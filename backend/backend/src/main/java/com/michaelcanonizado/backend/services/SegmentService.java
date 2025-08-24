package com.michaelcanonizado.backend.services;

import com.michaelcanonizado.backend.dtos.segment.SegmentDetailedDTO;
import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.entity.EntityNotFoundException;
import com.michaelcanonizado.backend.mapper.SegmentMapper;
import com.michaelcanonizado.backend.models.Segment;
import com.michaelcanonizado.backend.repositories.SegmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
