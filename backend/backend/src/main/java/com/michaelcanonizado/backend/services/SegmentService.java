package com.michaelcanonizado.backend.services;

import com.michaelcanonizado.backend.annotations.RequirePageantStatus;
import com.michaelcanonizado.backend.dtos.segment.SegmentCreateDTO;
import com.michaelcanonizado.backend.dtos.segment.SegmentDetailedDTO;
import com.michaelcanonizado.backend.dtos.segment.SegmentSummaryDTO;
import com.michaelcanonizado.backend.dtos.segment.SegmentUpdateDTO;
import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.customs.EntityNotFoundException;
import com.michaelcanonizado.backend.mappers.SegmentMapper;
import com.michaelcanonizado.backend.messages.OngoingSegmentMessage;
import com.michaelcanonizado.backend.models.*;
import com.michaelcanonizado.backend.repositories.*;
import com.michaelcanonizado.backend.contexts.PageantContext;
import com.michaelcanonizado.backend.specifications.CandidateSegmentQualificationSpecification;
import com.michaelcanonizado.backend.specifications.ScoreSpecification;
import com.michaelcanonizado.backend.specifications.SegmentSpecification;
import com.michaelcanonizado.backend.utilities.FormulaEncoder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
class CandidateResult {
    private UUID candidateId;
    private Double result;
}

@Service
public class SegmentService {
    @Autowired
    private SegmentRepository segmentRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private CandidateSegmentQualificationRepository csqRepository;

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private PageantRepository pageantRepository;

    @Autowired
    private SegmentMapper mapper;

    @Autowired
    private PageantContext pageantContext;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private FormulaEncoder formulaEncoder;

    @RequirePageantStatus({
            PageantStatus.PREPARATION
    })
    @Transactional
    public SegmentDetailedDTO addSegment(SegmentCreateDTO segmentCreateDTO) {
        /* Load DTO to entity */
        Segment segment = mapper.toEntity(segmentCreateDTO);

        /* Check if phase being connected actually belongs to the pageant */
        pageantContext.assertAccess(segment.getPhase().getPageant().getId());

        Segment savedSegment = segmentRepository.save(segment);

        /* Get current candidates and qualify them to the new segment */
        List<Candidate> candidates = candidateRepository.findAll();
        candidates.forEach(candidate -> {
            csqRepository.save(new CandidateSegmentQualification(savedSegment, candidate));
        });

        /* Save candidate to database */
        return mapper.toDetailedDTO(savedSegment);
    }

    @RequirePageantStatus({
            PageantStatus.ONGOING
    })
    @Transactional
    public SegmentDetailedDTO startSegment(UUID id) {
        Segment segment = segmentRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Cannot start! Segment not found.", ErrorCode.ENTITY_NOT_FOUND);
        });

        pageantContext.assertAccess(segment.getPhase().getPageant().getId());
        UUID selectedPageantId = pageantContext.getId();

        /* Determine the qualified candidates for the segment if a formula and candidate limit is present. */
        if (segment.getFormula() != null && segment.getCandidateLimit() != null) {
            /* Extract criterion ids from formula */
            Set<UUID> criteriaIdsInFormula = formulaEncoder.extractEncodedUUIDs(segment.getFormula());

            /* Get candidates */
            List<UUID> candidateIds = candidateRepository
                    .findAllByPageant_Id(selectedPageantId)
                    .stream()
                    .map(Candidate::getId)
                    .toList();

            /* Fetch relevant scores: */
            List<Score> scores = scoreRepository.findAll(
                    Specification.allOf(
                            /* Scores that belong to the selected pageant */
                            ScoreSpecification.hasPageant(selectedPageantId),
                            /* Scores that belong to the fetched candidates */
                            ScoreSpecification.hasCandidates(candidateIds),
                            /* Scores for criteria that is included in the formula */
                            ScoreSpecification.hasCriteria(new ArrayList<>(criteriaIdsInFormula))
                    )
            );

            /* Aggregate. Group the criterion averages per candidate */
            Map<UUID, Map<String, Double>> candidateCriterionAverages = scores
                    .stream()
                    .collect(
                            Collectors.groupingBy(
                                    score -> {
                                        return score.getCandidate().getId();
                                    },
                                    Collectors.groupingBy(score -> {
                                                return formulaEncoder.encodeUUID(score.getCriterion().getId());
                                            },
                                            Collectors.averagingInt(Score::getValue)
                                    )
                            )
                    );

            /* Get candidateSegmentQualification rows for the candidates */
            List<CandidateSegmentQualification> candidateSegmentQualifications = csqRepository.findAll(
                    Specification.allOf(
                            CandidateSegmentQualificationSpecification.hasSegment(segment.getId()),
                            CandidateSegmentQualificationSpecification.hasCandidates(candidateIds)
                    )
            );

            /* Turn the csq rows from a list to map for O(1) lookup */
            Map<UUID, CandidateSegmentQualification> candidateSegmentQualificationMap =
                    candidateSegmentQualifications
                            .stream()
                            .collect(
                                    Collectors.toMap(
                                            csqRow -> csqRow.getCandidate().getId(),
                                            Function.identity()
                                    )
                            );

            /* Create shared parser */
            ExpressionParser parser = new SpelExpressionParser();
            /* Parse the formula and reuse it. Parsing is expensive! */
            Expression expression = parser.parseExpression(segment.getFormula());
            /* Collect the results */
            List<CandidateResult> candidateResults = new ArrayList<>();
            /* Loop through all candidates and use their criterion averages to fill the formula */
            candidateCriterionAverages.forEach((candidateId, criterionAverages) -> {
                /* Load the criterion averages into context */
                StandardEvaluationContext context = new StandardEvaluationContext(criterionAverages);
                /* Substitute the criterion score average in the formula */
                criterionAverages.forEach(context::setVariable);
                /* Evaluate the expression */
                Double result = expression.getValue(context, Double.class);
                /* Push the result to the candidate result list */
                candidateResults.add(new CandidateResult(candidateId, result));
            });

            /* Sort results in descending order */
            candidateResults.sort((a, b) -> {
                return Double.compare(b.getResult(), a.getResult());
            });
            /* Get the qualified candidates */
            Set<UUID> qualifiedCandidateIds = candidateResults
                    .stream()
                    .limit(segment.getCandidateLimit())
                    .map(CandidateResult::getCandidateId)
                    .collect(Collectors.toSet());

            /* Update the CSQs. Batch save to lessen db queries */
            List<CandidateSegmentQualification> updatedCSQs = new ArrayList<>();
            for (CandidateSegmentQualification csq : candidateSegmentQualificationMap.values()) {
                UUID candidateId = csq.getCandidate().getId();
                if (qualifiedCandidateIds.contains(candidateId)) {
                    csq.setQualified(true);
                } else {
                    csq.setQualified(false);
                }
                updatedCSQs.add(csq);
            }
            csqRepository.saveAll(updatedCSQs);
        }


        /* TO-IMPLEMENT: Ensure that only 1 has the state ONGOING */
        segment.setStatus(PhaseSegmentStatus.ONGOING);

        /* Notify the client about the new ongoing segment */
        simpMessagingTemplate.convertAndSend(
                "/topic/pageants/" + selectedPageantId + "/ongoing-segment",
                new OngoingSegmentMessage(segment.getId())
        );

        return mapper.toDetailedDTO(segmentRepository.save(segment));
    }

    @RequirePageantStatus({
            PageantStatus.ONGOING
    })
    @Transactional
    public SegmentDetailedDTO closeSegment(UUID id) {
        Segment segment = segmentRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Cannot close! Segment not found.", ErrorCode.ENTITY_NOT_FOUND);
        });

        pageantContext.assertAccess(segment.getPhase().getPageant().getId());
        UUID selectedPageantId = pageantContext.getId();

        segment.setStatus(PhaseSegmentStatus.CLOSED);

        /* Notify the client about the new ongoing segment */
        simpMessagingTemplate.convertAndSend(
                "/topic/pageants/" + selectedPageantId + "/ongoing-segment",
                new OngoingSegmentMessage(null)
        );

        return mapper.toDetailedDTO(segmentRepository.save(segment));
    }

    @RequirePageantStatus({
            PageantStatus.PREPARATION,
            PageantStatus.ONGOING
    })
    @Transactional
    public SegmentDetailedDTO getSegment(UUID id) {
        Segment segment = segmentRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Segment not found!", ErrorCode.ENTITY_NOT_FOUND);
        });
        pageantContext.assertAccess(segment.getPhase().getPageant().getId());

        if (segment.getFormula() != null) {
            String encodedFormula = segment.getFormula();
            String decodedFormula = formulaEncoder.encodeFormula(encodedFormula);
            segment.setFormula(decodedFormula);
        }

        return mapper.toDetailedDTO(segment);
    }

    @RequirePageantStatus({
            PageantStatus.PREPARATION,
            PageantStatus.ONGOING
    })
    @Transactional
    public List<SegmentSummaryDTO> getSegments() {
        UUID selectedPageantId = pageantContext.getId();
        return segmentRepository.findAll(
                Specification.allOf(
                        SegmentSpecification.hasPageant(selectedPageantId)
                )
        ).stream().sorted(
                Comparator.comparing((Segment segment) -> segment.getPhase().getSequence())
                        .thenComparing(Segment::getSequence)
        ).map(segment -> {
            if (segment.getFormula() != null) {
                String encodedFormula = segment.getFormula();
                String decodedFormula = formulaEncoder.encodeFormula(encodedFormula);
                segment.setFormula(decodedFormula);
            }
            return mapper.toSummaryDTO(segment);
        }).toList();
    }

    @RequirePageantStatus({
            PageantStatus.PREPARATION
    })
    @Transactional
    public SegmentSummaryDTO updateSegment(UUID id, SegmentUpdateDTO segmentUpdateDTO) {
        Segment segment = segmentRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Can't update! Segment not found.", ErrorCode.ENTITY_NOT_FOUND);
        });
        pageantContext.assertAccess(segment.getPhase().getPageant().getId());

        mapper.updateEntityFromDTO(segment, segmentUpdateDTO);

        if (segment.getFormula() != null) {
            String rawFormula = segment.getFormula();
            String encodedFormula = formulaEncoder.encodeFormula(rawFormula);
            segment.setFormula(encodedFormula);
        }

        return mapper.toSummaryDTO(segmentRepository.save(segment));
    }

    @RequirePageantStatus({
            PageantStatus.PREPARATION
    })
    public void deleteSegment(UUID id) {
        Segment segment = segmentRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Can't delete! Segment not found.", ErrorCode.ENTITY_NOT_FOUND);
        });
        pageantContext.assertAccess(segment.getPhase().getPageant().getId());
        segmentRepository.deleteById(id);
    }
}
