package com.michaelcanonizado.backend.services;

import com.michaelcanonizado.backend.annotations.RequirePageantStatus;
import com.michaelcanonizado.backend.dtos.segment.*;
import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.customs.EntityNotFoundException;
import com.michaelcanonizado.backend.exceptions.customs.PhaseSegmentStatusException;
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

import java.util.*;

@Service
public class SegmentService {
    @Autowired
    private SegmentRepository segmentRepository;

    @Autowired
    private SegmentMapper segmentMapper;

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
        UUID selectedPageantId = pageantContext.getId();

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
}
