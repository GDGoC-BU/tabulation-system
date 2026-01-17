package com.michaelcanonizado.backend.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.michaelcanonizado.backend.annotations.RequirePageantStatus;
import com.michaelcanonizado.backend.dtos.leaderboard.LeaderboardDetailedDTO;
import com.michaelcanonizado.backend.dtos.segment.*;
import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.customs.EntityNotFoundException;
import com.michaelcanonizado.backend.exceptions.customs.PhaseSegmentStatusException;
import com.michaelcanonizado.backend.formula.blocks.*;
import com.michaelcanonizado.backend.mappers.*;
import com.michaelcanonizado.backend.messages.OngoingSegmentMessage;
import com.michaelcanonizado.backend.models.*;
import com.michaelcanonizado.backend.repositories.*;
import com.michaelcanonizado.backend.contexts.PageantContext;
import com.michaelcanonizado.backend.specifications.SegmentSpecification;
import com.michaelcanonizado.backend.utilities.CacheKeyBuilder;
import com.michaelcanonizado.backend.utilities.CacheNameConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SegmentService {
    @Autowired
    private SegmentRepository segmentRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private PhaseRepository phaseRepository;

    @Autowired
    private LeaderboardService leaderboardService;

    @Autowired
    private SegmentMapper segmentMapper;

    @Autowired
    private LeaderboardMapper leaderboardMapper;

    @Autowired
    private PageantMapper pageantMapper;

    @Autowired
    private PhaseMapper phaseMapper;

    @Autowired
    private PageantContext pageantContext;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private CacheKeyBuilder cacheKeyBuilder;

    @RequirePageantStatus({
            PageantStatus.PREPARATION
    })
    @Transactional
    public SegmentDetailedDTO addSegment(SegmentCreateDTO segmentCreateDTO) {
        Segment segment = segmentMapper.toEntity(segmentCreateDTO);
        pageantContext.assertAccess(segment.getPhase().getPageant().getId());

        /* Verify qualification leaderboard formula if there is */

        Segment savedSegment = segmentRepository.save(segment);
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

        /* If this is a qualifying segment, reflect the selected candidates in
        * qualification leaderboard in ranking leaderboard. */
        if (segment.isQualificationRequired()) {
            Set<UUID> selectedCandidateIds = segment
                    .getQualificationLeaderboard()
                    .getSelectedCandidates()
                    .stream()
                    .map(Candidate::getId)
                    .collect(Collectors.toSet());

            segment
                    .getRankingLeaderboard()
                    .getEntries()
                    .forEach(entry -> {
                        entry.setSelected(selectedCandidateIds.contains(entry.getCandidate().getId()));
                    });
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

        UUID selectedPageantId = pageantContext.getId();
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
        return segmentMapper.toDetailedDTO(segment);
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


    @Transactional
    public LeaderboardDetailedDTO calculateQualificationLeaderboard(UUID segmentId) {
        Segment segment = segmentRepository.findById(segmentId).orElseThrow(() -> {
            return new EntityNotFoundException(
                    "Cannot calculate qualification leaderboard! Segment not found.",
                    ErrorCode.ENTITY_NOT_FOUND
            );
        });
        pageantContext.assertAccess(segment.getPhase().getPageant().getId());

        if (!segment.isQualificationRequired()) {
            // Throw custom error
            throw new RuntimeException("");
        }

        /* Get previous segment within the same phase */
        Segment previousSegmentInTheSamePhase = segment
                .getPhase()
                .getSegments()
                .stream()
                .filter(s -> s.getSequence() < segment.getSequence())
                .max(Comparator.comparingInt(Segment::getSequence))
                .orElse(null);

        List<Candidate> candidatesToEvaluate;

        /* Funneling effect. Candidates who were in the previous segment should be the only
        * ones to be evaluated to be qualified for the current segment.
        *
        * Segment.qualificationLeaderboard will contain fewer entries as the pageant moves on. */
        if (previousSegmentInTheSamePhase == null) {
            /* Rare edge case where the starting segment of a phase already has qualifications.
            *
            * Case 1:
            * P1: [S1,S2,S3], P2: [S1(q),S2(q)]
            * - P2.S1 should refer to P1.S3
            *
            * Case 2:
            * P1: [S1(q),S2(q)], P2: [S1,S2,S3]
            * - This is an invalid pageant. What even is the criteria for P1.S1?
            *   Since it's the very first segment in the WHOLE pageant.
            * - Formula should also be verified such that it doesn't contain criteria
            *   that comes after the segment it's applied on. */
            UUID selectedPageantId = pageantContext.getId();
            List<Phase> phases = phaseRepository.findAllByPageant_Id(selectedPageantId);

            /* Find the previous phase */
            Phase currentPhase = segment.getPhase();
            Phase previousPhase = phases
                    .stream()
                    .filter(phase -> phase.getSequence() < currentPhase.getSequence())
                    .max(Comparator.comparingInt(Phase::getSequence))
                    .orElse(null);

            /* Case 2 Error */
            if (previousPhase == null) {
                // First phase, first segment
                throw new RuntimeException("");
            }

            /* Get the last segment in that phase */
            Segment lastSegmentInPreviousPhase = previousPhase
                    .getSegments()
                    .stream()
                    .max(Comparator.comparingInt(Segment::getSequence))
                    .orElse(null);

            /* Previous phase has no segments */
            if (lastSegmentInPreviousPhase == null) {
                // Phase before has no segments
                throw new RuntimeException("");
            }

            candidatesToEvaluate = lastSegmentInPreviousPhase
                    .getRankingLeaderboard()
                    .getSelectedCandidates();

        } else {
            candidatesToEvaluate = previousSegmentInTheSamePhase
                    .getRankingLeaderboard()
                    .getSelectedCandidates();
        }

        Leaderboard updatedLeaderboard = leaderboardService.calculateLeaderboard(
                segment.getQualificationLeaderboard(),
                candidatesToEvaluate
        );
        return leaderboardMapper.toDetailedDTO(updatedLeaderboard);
    }

    public LeaderboardDetailedDTO getQualificationLeaderboard(UUID segmentId) {
        Segment segment = segmentRepository.findById(segmentId).orElseThrow(() -> {
            return new EntityNotFoundException(
                    "Cannot get qualification leaderboard! Segment not found.",
                    ErrorCode.ENTITY_NOT_FOUND
            );
        });
        pageantContext.assertAccess(segment.getPhase().getPageant().getId());
        return leaderboardMapper.toDetailedDTO(segment.getQualificationLeaderboard());
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
        return segmentRepository
                .findAll(
                    Specification.allOf(
                            SegmentSpecification.hasPageant(selectedPageantId)
                    )
                )
                .stream()
                .sorted(
                    Comparator
                            .comparing((Segment segment) -> segment.getPhase().getSequence())
                            .thenComparing(Segment::getSequence)
                )
                .map(segment -> segmentMapper.toSummaryDTO(segment))
                .toList();
    }

    @RequirePageantStatus({
            PageantStatus.PREPARATION
    })
    @Transactional
    public SegmentDetailedDTO updateSegment(UUID id, SegmentUpdateDTO segmentUpdateDTO) {
        Segment segment = segmentRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Can't update! Segment not found.", ErrorCode.ENTITY_NOT_FOUND);
        });
        pageantContext.assertAccess(segment.getPhase().getPageant().getId());
        segmentMapper.updateEntityFromDTO(segment, segmentUpdateDTO);
        return segmentMapper.toDetailedDTO(segmentRepository.save(segment));
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

    @Transactional
    public void initializeSegment(UUID pageantId) {
        List<Segment> segments = segmentRepository.findAll(
                Specification.allOf(
                        SegmentSpecification.hasPageant(pageantId)
                )
        );
        List<Candidate> candidates = candidateRepository.findAllByPageant_Id(pageantId);

        JsonNode emptyWorkspace = new ObjectMapper().createObjectNode();
        /* Initialize ranking leaderboards for all segments */
        segments.forEach(segment -> {
            /* Create formula for the leaderboard */
            List<Criterion> criteria = segment.getCriteria();
            if (criteria.isEmpty()) {
                // Throw, segment has no criteria
            }

            /* Add up all criteria in the segment */
            BlockNode root = new CriterionNode(criteria.getFirst().getId());
            for (int i = 1; i < criteria.size(); i++) {
                root = new BinaryOperationNode(
                        root,
                        BinaryOperator.ADD,
                        new CriterionNode(criteria.get(i).getId())
                );
            }
            String formulaText = criteria.stream()
                    .map(c -> String.valueOf(c.getId()))
                    .collect(Collectors.joining(" + "));

            /* Create a leaderboard */
            Leaderboard rankingLeaderboard = new Leaderboard(
                    new Formula(formulaText, emptyWorkspace, root),
                    4
            );

            /* Create entries for all candidates */
            candidates.forEach(candidate -> {
                LeaderboardEntry entry = new LeaderboardEntry(candidate);
                entry.setSelected(true);
                rankingLeaderboard.addEntry(entry);
            });
            segment.setRankingLeaderboard(rankingLeaderboard);
        });

        segmentRepository.saveAll(segments);
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
}
