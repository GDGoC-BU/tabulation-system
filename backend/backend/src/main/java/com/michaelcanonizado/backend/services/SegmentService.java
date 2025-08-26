package com.michaelcanonizado.backend.services;

import com.michaelcanonizado.backend.dtos.segment.SegmentCreateDTO;
import com.michaelcanonizado.backend.dtos.segment.SegmentDetailedDTO;
import com.michaelcanonizado.backend.dtos.segment.SegmentSummaryDTO;
import com.michaelcanonizado.backend.dtos.segment.SegmentUpdateDTO;
import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.entity.EntityMismatchException;
import com.michaelcanonizado.backend.exceptions.entity.EntityNotFoundException;
import com.michaelcanonizado.backend.mappers.SegmentMapper;
import com.michaelcanonizado.backend.models.Segment;
import com.michaelcanonizado.backend.repositories.SegmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SegmentService {
    @Autowired
    private SegmentRepository repository;

    @Autowired
    private SegmentMapper mapper;

    public SegmentDetailedDTO getSegment(UUID id) {
        Segment segment = repository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Segment of id " + " not found!", ErrorCode.SEGMENT_NOT_FOUND);
        });

        return mapper.toDetailedDTO(segment);
    }

    public List<SegmentSummaryDTO> getSegments() {
        return repository.findAll().stream().map(segment -> {
            return mapper.toSummaryDTO(segment);
        }).toList();
    }

    public SegmentDetailedDTO addSegment(SegmentCreateDTO segmentCreateDTO) {
        Segment segment = mapper.toEntity(segmentCreateDTO);
        return mapper.toDetailedDTO(repository.save(segment));
    }

    public SegmentSummaryDTO updateSegment(UUID id, SegmentUpdateDTO segmentUpdateDTO) {
        if (!id.equals(segmentUpdateDTO.id())) {
            throw new EntityMismatchException(
                    "Path id " + id + " and Body.id " + segmentUpdateDTO.id() + " doesn't match.",
                    ErrorCode.SEGMENT_MISMATCH
            );
        }

        Segment segment = repository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Segment of id " + id + " doesn't exist.", ErrorCode.SEGMENT_NOT_FOUND);
        });
        mapper.updateEntityFromDTO(segment, segmentUpdateDTO);
        return mapper.toSummaryDTO(repository.save(segment));
    }

    public void deleteSegment(UUID id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Deletion failed! Segment of id " + id + " not found.", ErrorCode.SEGMENT_NOT_FOUND);
        }

        repository.deleteById(id);
    }
}
