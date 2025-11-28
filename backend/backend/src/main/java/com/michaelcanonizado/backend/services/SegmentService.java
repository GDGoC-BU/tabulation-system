package com.michaelcanonizado.backend.services;

import com.michaelcanonizado.backend.annotations.RequirePageantStatus;
import com.michaelcanonizado.backend.dtos.criterion.CriterionBreakdownDTO;
import com.michaelcanonizado.backend.dtos.pageant.PageantHierarchyDTO;
import com.michaelcanonizado.backend.dtos.phase.PhaseBreakdownDTO;
import com.michaelcanonizado.backend.dtos.score.ScoreBreakdownDTO;
import com.michaelcanonizado.backend.dtos.segment.*;
import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.customs.EntityNotFoundException;
import com.michaelcanonizado.backend.exceptions.customs.PhaseSegmentStatusException;
import com.michaelcanonizado.backend.mappers.*;
import com.michaelcanonizado.backend.messages.OngoingSegmentMessage;
import com.michaelcanonizado.backend.models.*;
import com.michaelcanonizado.backend.repositories.*;
import com.michaelcanonizado.backend.contexts.PageantContext;
import com.michaelcanonizado.backend.specifications.CandidateSegmentQualificationSpecification;
import com.michaelcanonizado.backend.specifications.ScoreSpecification;
import com.michaelcanonizado.backend.specifications.SegmentSpecification;
import com.michaelcanonizado.backend.utilities.CacheKeyBuilder;
import com.michaelcanonizado.backend.utilities.CacheNameConstants;
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
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    private CriterionRepository criterionRepository;

    @Autowired
    private PageantMapper pageantMapper;

    @Autowired
    private PhaseMapper phaseMapper;

    @Autowired
    private SegmentMapper segmentMapper;

    @Autowired
    private CriterionMapper criterionMapper;

    @Autowired
    private JudgeMapper judgeMapper;

    @Autowired
    private ScoreMapper scoreMapper;

    @Autowired
    private PageantContext pageantContext;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private FormulaEncoder formulaEncoder;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private CacheKeyBuilder cacheKeyBuilder;

    @RequirePageantStatus({
            PageantStatus.PREPARATION
    })
    @Transactional
    public SegmentDetailedDTO addSegment(SegmentCreateDTO segmentCreateDTO) {
        /* Load DTO to entity */
        Segment segment = segmentMapper.toEntity(segmentCreateDTO);

        /* Check if phase being connected actually belongs to the pageant */
        pageantContext.assertAccess(segment.getPhase().getPageant().getId());

        Segment savedSegment = segmentRepository.save(segment);

        /* Get current candidates and qualify them to the new segment */
        List<Candidate> candidates = candidateRepository.findAll();
        candidates.forEach(candidate -> {
            csqRepository.save(new CandidateSegmentQualification(savedSegment, candidate));
        });

        /* Save candidate to database */
        return segmentMapper.toDetailedDTO(savedSegment);
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

        boolean shouldCalculateCandidateQualifications =
                segment.getFormula() != null && segment.getCandidateLimit() != null;

        if (shouldCalculateCandidateQualifications) {
            calculateCandidateQualificationsHelper(segment, selectedPageantId);
        }

        /* TO-IMPLEMENT: Ensure that only 1 has the state ONGOING */
        segment.setStatus(PhaseSegmentStatus.ONGOING);
        Segment savedSegment = segmentRepository.save(segment);

        /* Update cache immediately to prevent cache stampede. For instance, Judges
           will refetch /pageants/{id}/hierarchy at the same time when they get notified.
           Can be improved using thread locks but that's pretty overkill. But for now,
           just update all cache instances that hold the segment */
        Phase phase = segment.getPhase();
        Pageant pageant = phase.getPageant();
        updateCacheThatHaveSegment(pageant, phase);

        /* Notify the client about the new ongoing segment after database commit */
        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        simpMessagingTemplate.convertAndSend(
                                "/topic/pageants/" + selectedPageantId + "/ongoing-segment",
                                new OngoingSegmentMessage(savedSegment.getId())
                        );
                    }
                }
        );
        return segmentMapper.toDetailedDTO(savedSegment);
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
        segment.setStatus(PhaseSegmentStatus.CLOSED);
        Segment savedSegment = segmentRepository.save(segment);

        Phase phase = segment.getPhase();
        Pageant pageant = phase.getPageant();
        updateCacheThatHaveSegment(pageant, phase);

        UUID selectedPageantId = pageantContext.getId();
        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        simpMessagingTemplate.convertAndSend(
                                "/topic/pageants/" + selectedPageantId + "/ongoing-segment",
                                new OngoingSegmentMessage(null)
                        );
                    }
                }
        );
        return segmentMapper.toDetailedDTO(savedSegment);
    }

    private void updateCacheThatHaveSegment(Pageant pageant, Phase phase) {
        /* Pageant Hierarchy */
        cacheService.put(
                CacheNameConstants.TABULATION,
                cacheKeyBuilder.build("pageants", pageant.getId(), "hierarchy"),
                pageantMapper.toHierarchyDTO(pageant)
        );

        /* Phase */
        cacheService.put(
                CacheNameConstants.TABULATION,
                cacheKeyBuilder.build("pageants", pageant.getId(), "phases", phase.getId()),
                phaseMapper.toDetailedDTO(phase)
        );

        /* Ongoing Phase */
        if (phase.getStatus() == PhaseSegmentStatus.ONGOING) {
            cacheService.put(
                    CacheNameConstants.TABULATION,
                    cacheKeyBuilder.build("pageants", pageant.getId(), "phases", "ongoing"),
                    phaseMapper.toDetailedDTO(phase)
            );
        }
    }

    @RequirePageantStatus({
            PageantStatus.PREPARATION,
            PageantStatus.ONGOING,
            PageantStatus.FINALIZING,
            PageantStatus.CLOSED,
    })
    @Transactional
    public SegmentDetailedDTO calculateCandidateQualifications(UUID id) {
        Segment segment = segmentRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Cannot start! Segment not found.", ErrorCode.ENTITY_NOT_FOUND);
        });

        pageantContext.assertAccess(segment.getPhase().getPageant().getId());

        /* If the formula or candidate limit is not present, Don't determine the qualified
           candidates for the segment.

           NOTE: There should be a funnel effect here! Suppose the previous segment has a
           candidate limit and formula, and the next segment has none. THe qualified candidates
           from the previous segment should be the only qualified for the segment, not all
           candidates! It should have a funnel effect. */
        if (segment.getFormula() == null || segment.getCandidateLimit() == null) {
            return segmentMapper.toDetailedDTO(segmentRepository.save(segment));
        }

        UUID selectedPageantId = pageantContext.getId();
        calculateCandidateQualificationsHelper(segment, selectedPageantId);

        return segmentMapper.toDetailedDTO(segmentRepository.save(segment));
    }

    private void calculateCandidateQualificationsHelper(Segment segment, UUID pageantId) {
        /* NOTE: Refer to AwardService for a more detailed documentation of the flow.
           This method is just a reflection of the logic used there with minor tweaks. */

        /* Extract criterion ids from formula */
        Set<UUID> criteriaIdsInFormula = formulaEncoder.extractEncodedUUIDs(segment.getFormula());

        /* Fetch the criteria in the formula */
        List<Criterion> criteriaInFormula = criterionRepository.findAllById(criteriaIdsInFormula);

        /* Load criteria in a Map for faster lookup */
        Map<UUID, Criterion> criteriaMap = criteriaInFormula
                .stream()
                .collect(
                        Collectors.toMap(
                                Criterion::getId,
                                Function.identity(),
                                (existing, replacement) -> existing
                        )
                );

        /* Pregenerate the phase-segment-criterion details for the breakdowns. */
        Map<UUID, CriteriaBreakdown> criteriaBreakdownTemplates = new HashMap<>();
        for (UUID criterionId : criteriaIdsInFormula) {
            Criterion criterion = criteriaMap.get(criterionId);

            PhaseBreakdownDTO phaseBreakdown = phaseMapper.toBreakdownDTO(criterion.getSegment().getPhase());
            SegmentBreakdownDTO segmentBreakdown = segmentMapper.toBreakdownDTO(criterion.getSegment());
            CriterionBreakdownDTO criterionBreakdown = criterionMapper.toBreakdownDTO(criterion);

            criteriaBreakdownTemplates.put(
                    criterionId,
                    new CriteriaBreakdown(
                            phaseBreakdown,
                            segmentBreakdown,
                            criterionBreakdown,
                            null,
                            null
                    )
            );
        }

        /* Get candidates and construct the necessary Maps */
        List<Candidate> candidates = candidateRepository
                .findAllByPageant_Id(pageantId);

        Map<UUID, CandidateGender> candidateGenders = candidates
                .stream()
                .collect(Collectors.toMap(
                        Candidate::getId,
                        Candidate::getGender
                ));

        List<UUID> candidateIds = candidates
                .stream()
                .map(Candidate::getId)
                .toList();

        /* Fetch relevant scores: */
        List<Score> scores = scoreRepository.findAll(
                Specification.allOf(
                        /* Scores that belong to the selected pageant */
                        ScoreSpecification.hasPageant(pageantId),
                        /* Scores that belong to the fetched candidates */
                        ScoreSpecification.hasCandidates(candidateIds),
                        /* Scores for criteria that is included in the formula */
                        ScoreSpecification.hasCriteria(new ArrayList<>(criteriaIdsInFormula))
                )
        );

        /* Map to search for scores for a candidate over a criterion
           Map<CandidateID, Map<CriterionId, List<Score>>> */
        Map<UUID, Map<UUID, List<Score>>> scoreMap = new HashMap<>();
        for (Score score : scores) {
            UUID candidateId = score.getCandidate().getId();
            UUID criterionId = score.getCriterion().getId();

            scoreMap
                    .computeIfAbsent(candidateId, k -> new HashMap<>())
                    .computeIfAbsent(criterionId, k -> new ArrayList<>())
                    .add(score);
        }

        /* Group the criterion averages per candidate
           Map<CandidateId, Map<EncodedCriterionId, Average Score>> */
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

        /* Get candidateSegmentQualification rows of the candidates */
        List<CandidateSegmentQualification> candidateSegmentQualifications = csqRepository.findAll(
                Specification.allOf(
                        CandidateSegmentQualificationSpecification.hasSegment(segment.getId()),
                        CandidateSegmentQualificationSpecification.hasCandidates(candidateIds)
                )
        );
        /* Map<CandidateId, CSQ> */
        Map<UUID, CandidateSegmentQualification> csqMap =
                candidateSegmentQualifications
                        .stream()
                        .collect(
                                Collectors.toMap(
                                        csqRow -> csqRow.getCandidate().getId(),
                                        Function.identity()
                                )
                        );

        /* Collect the results */
        List<CandidateSegmentQualification> femaleCandidateResults = new ArrayList<>();
        List<CandidateSegmentQualification> maleCandidateResults = new ArrayList<>();

        /* Create shared parser */
        ExpressionParser parser = new SpelExpressionParser();
        /* Parse the formula and reuse it. Parsing is expensive! */
        Expression expression = parser.parseExpression(segment.getFormula());
        /* Loop through all candidates and use their criterion averages to fill the formula */
        candidateCriterionAverages.forEach((candidateId, criterionAverages) -> {
            /* Load the criterion averages into context */
            StandardEvaluationContext context = new StandardEvaluationContext(criterionAverages);
            /* Substitute the criterion score average in the formula */
            criterionAverages.forEach(context::setVariable);
            /* Evaluate the expression */
            Double result = expression.getValue(context, Double.class);

            CandidateSegmentQualification csq = csqMap.get(candidateId);
            csq.setScore(result);

            List<CriteriaBreakdown> criteriaBreakdowns = new ArrayList<>();
            criteriaIdsInFormula.forEach(criterionId -> {
                /* Get scores for the current candidate and current criterion */
                List<Score> scoresForCriterion = scoreMap
                        .getOrDefault(candidateId, Map.of())
                        .getOrDefault(criterionId, List.of());

                /* Get pregenerated breakdown for the current criterion */
                CriteriaBreakdown breakdownTemplate = criteriaBreakdownTemplates.get(criterionId);

                /* Get the calculate score from the existing map. */
                Double averageScore = candidateCriterionAverages
                        .getOrDefault(candidateId, Collections.emptyMap())
                        .getOrDefault(formulaEncoder.encodeUUID(criterionId), 0.0);

                List<ScoreBreakdownDTO> scoresBreakdown = scoresForCriterion
                        .stream()
                        .map(score -> {
                            return scoreMapper.toBreakdownDTO(score);
                        }).toList();

                CriteriaBreakdown criteriaBreakdown = new CriteriaBreakdown(
                        breakdownTemplate.getPhase(),
                        breakdownTemplate.getSegment(),
                        breakdownTemplate.getCriterion(),
                        averageScore,
                        scoresBreakdown
                );
                criteriaBreakdowns.add(criteriaBreakdown);
            });
            csq.setCriteriaBreakdown(criteriaBreakdowns);

            /* Group the CSQs by gender */
            if (candidateGenders.get(candidateId).equals(CandidateGender.FEMALE)) {
                femaleCandidateResults.add(csq);
            } else if (candidateGenders.get(candidateId).equals(CandidateGender.MALE)) {
                maleCandidateResults.add(csq);
            }
        });

        /* Sort scores in descending order */
        femaleCandidateResults.sort((a, b) -> {
            return Double.compare(b.getScore(), a.getScore());
        });
        maleCandidateResults.sort((a, b) -> {
            return Double.compare(b.getScore(), a.getScore());
        });

        /* Determine CSQ rank, isQualified, and isTied */
        for (int i = 0; i < femaleCandidateResults.size(); i++) {
            int currentRank = i + 1;
            CandidateSegmentQualification currentCSQ = femaleCandidateResults.get(i);
            Double currentScore = currentCSQ.getScore();

            /* Set the rank */
            currentCSQ.setRank(currentRank);
            /* Mark as qualified if within the candidate limit */
            currentCSQ.setQualified(currentRank <= segment.getCandidateLimit());

            /* Count the number of tied candidates so we can just offset if there is a tie */
            int tieCount = 0;
            /* Check following candidates for ties */
            for (int j = i + 1; j < femaleCandidateResults.size(); j++) {
                CandidateSegmentQualification nextCSQ = femaleCandidateResults.get(j);

                /* If the next candidate has a different score, i.e: no tie, break out */
                if (Math.abs(nextCSQ.getScore() - currentScore) > 1e-9) break;

                /* Tied candidates will have the same: rank, isQualified, and tie values */
                nextCSQ.setRank(currentRank);
                nextCSQ.setQualified(currentRank <= segment.getCandidateLimit());
                nextCSQ.setTied(true);
                currentCSQ.setTied(true);

                tieCount++;
            }

            /* Skip over tied candidates since we have already set their fields */
            i += tieCount;
        }

        for (int i = 0; i < maleCandidateResults.size(); i++) {
            int currentRank = i + 1;
            CandidateSegmentQualification currentCSQ = maleCandidateResults.get(i);
            Double currentScore = currentCSQ.getScore();

            /* Set the rank */
            currentCSQ.setRank(currentRank);
            /* Mark as qualified if within the candidate limit */
            currentCSQ.setQualified(currentRank <= segment.getCandidateLimit());

            /* Count the number of tied candidates so we can just offset if there is a tie */
            int tieCount = 0;
            /* Check following candidates for ties */
            for (int j = i + 1; j < maleCandidateResults.size(); j++) {
                CandidateSegmentQualification nextCSQ = maleCandidateResults.get(j);

                /* If the next candidate has a different score, i.e: no tie, break out */
                if (Math.abs(nextCSQ.getScore() - currentScore) > 1e-9) break;

                /* Tied candidates will have the same: rank, isQualified, and tie values */
                nextCSQ.setRank(currentRank);
                nextCSQ.setQualified(currentRank <= segment.getCandidateLimit());
                nextCSQ.setTied(true);
                currentCSQ.setTied(true);

                tieCount++;
            }

            /* Skip over tied candidates since we have already set their fields */
            i += tieCount;
        }

        csqRepository.saveAll(femaleCandidateResults);
        csqRepository.saveAll(maleCandidateResults);
    }

    @RequirePageantStatus({
            PageantStatus.PREPARATION,
            PageantStatus.ONGOING,
            PageantStatus.FINALIZING,
            PageantStatus.ONGOING
    })
    @Transactional
    public SegmentDetailedDTO getSegment(UUID id) {
        Segment segment = segmentRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Segment not found!", ErrorCode.ENTITY_NOT_FOUND);
        });
        pageantContext.assertAccess(segment.getPhase().getPageant().getId());
        return segmentMapper.toDetailedDTO(segment);
    }

    @RequirePageantStatus({
            PageantStatus.PREPARATION,
            PageantStatus.ONGOING,
            PageantStatus.FINALIZING,
            PageantStatus.CLOSED
    })
    public SegmentDetailedDTO getOngoingSegment() {
        UUID selectedPageantId = pageantContext.getId();

        /* Revisit this. Might want to add a more robust check to verify that only 1 segment should be ongoing */
        List<Segment> ongoingSegments = segmentRepository
                .findAllByStatusAndPhasePageantId(
                        PhaseSegmentStatus.ONGOING,
                        selectedPageantId
                );

        /* Only 1 segment should be ongoing */
        if (ongoingSegments.size() > 1) {
            throw new PhaseSegmentStatusException(
                    "Multiple ongoing segments found for pageant " + selectedPageantId,
                    ErrorCode.PHASE_SEGMENT_ILLEGAL_STATE
            );
        }

        return ongoingSegments.stream()
                .findFirst()
                .map(segmentMapper::toDetailedDTO)
                .orElse(null);
    }

    @RequirePageantStatus({
            PageantStatus.PREPARATION,
            PageantStatus.ONGOING,
            PageantStatus.FINALIZING,
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
            return segmentMapper.toSummaryDTO(segment);
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

        segmentMapper.updateEntityFromDTO(segment, segmentUpdateDTO);

        Segment savedSegment = segmentRepository.save(segment);
        return segmentMapper.toSummaryDTO(savedSegment);
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
