package com.michaelcanonizado.backend.services;

import com.michaelcanonizado.backend.dtos.segment.SegmentCreateDTO;
import com.michaelcanonizado.backend.dtos.segment.SegmentDetailedDTO;
import com.michaelcanonizado.backend.dtos.segment.SegmentSummaryDTO;
import com.michaelcanonizado.backend.dtos.segment.SegmentUpdateDTO;
import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.entity.EntityMismatchException;
import com.michaelcanonizado.backend.exceptions.entity.EntityNotFoundException;
import com.michaelcanonizado.backend.mappers.SegmentMapper;
import com.michaelcanonizado.backend.models.Candidate;
import com.michaelcanonizado.backend.models.CandidateSegmentQualification;
import com.michaelcanonizado.backend.models.Segment;
import com.michaelcanonizado.backend.repositories.CandidateRepository;
import com.michaelcanonizado.backend.repositories.CandidateSegmentQualificationRepository;
import com.michaelcanonizado.backend.repositories.SegmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    private SegmentMapper mapper;

    @Transactional
    public SegmentDetailedDTO addSegment(SegmentCreateDTO segmentCreateDTO) {
        /* Load DTO to entity */
        Segment segment = mapper.toEntity(segmentCreateDTO);
        Segment savedSegment = segmentRepository.save(segment);

        /* Get current candidates and qualify them to the new segment */
        List<Candidate> candidates = candidateRepository.findAll();
        candidates.forEach(candidate -> {
            csqRepository.save(new CandidateSegmentQualification(savedSegment, candidate));
        });

        /* Save candidate to database */
        return mapper.toDetailedDTO(savedSegment);
    }

    @Transactional
    public SegmentDetailedDTO getSegment(UUID id) {
        Segment segment = segmentRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Segment of id " + " not found!", ErrorCode.SEGMENT_NOT_FOUND);
        });

        return mapper.toDetailedDTO(segment);
    }

    @Transactional
    public List<SegmentSummaryDTO> getSegments() {
        return segmentRepository
                .findAll()
                .stream()
                .sorted(Comparator.comparing(Segment::getPhase))
                .map(mapper::toSummaryDTO)
                .toList();
    }

    @Transactional
    public SegmentSummaryDTO updateSegment(UUID id, SegmentUpdateDTO segmentUpdateDTO) {
        Segment segment = segmentRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Segment of id " + id + " doesn't exist.", ErrorCode.SEGMENT_NOT_FOUND);
        });

        mapper.updateEntityFromDTO(segment, segmentUpdateDTO);
        return mapper.toSummaryDTO(segmentRepository.save(segment));
    }

    public void deleteSegment(UUID id) {
        if (!segmentRepository.existsById(id)) {
            throw new EntityNotFoundException("Deletion failed! Segment of id " + id + " not found.", ErrorCode.SEGMENT_NOT_FOUND);
        }

        segmentRepository.deleteById(id);
    }
}
