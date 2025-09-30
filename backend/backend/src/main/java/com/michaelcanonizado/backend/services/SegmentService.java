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
import com.michaelcanonizado.backend.repositories.CandidateRepository;
import com.michaelcanonizado.backend.repositories.CandidateSegmentQualificationRepository;
import com.michaelcanonizado.backend.repositories.PageantRepository;
import com.michaelcanonizado.backend.repositories.SegmentRepository;
import com.michaelcanonizado.backend.contexts.PageantContext;
import com.michaelcanonizado.backend.specifications.SegmentSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class SegmentService {
    @Autowired
    private SegmentRepository segmentRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private CandidateSegmentQualificationRepository csqRepository;

    @Autowired
    private PageantRepository pageantRepository;

    @Autowired
    private SegmentMapper mapper;

    @Autowired
    private PageantContext pageantContext;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

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
