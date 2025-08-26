package com.michaelcanonizado.backend.services;

import com.michaelcanonizado.backend.dtos.criterion.CriterionCreateDTO;
import com.michaelcanonizado.backend.dtos.criterion.CriterionSummaryDTO;
import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.entity.EntityNotFoundException;
import com.michaelcanonizado.backend.mappers.CriterionMapper;
import com.michaelcanonizado.backend.models.Criterion;
import com.michaelcanonizado.backend.models.Segment;
import com.michaelcanonizado.backend.repositories.CriterionRepository;
import com.michaelcanonizado.backend.repositories.SegmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CriterionService {
    @Autowired
    private CriterionRepository criterionRepository;

    @Autowired
    private SegmentRepository segmentRepository;

    @Autowired
    private CriterionMapper mapper;

    public CriterionSummaryDTO addCriterion(CriterionCreateDTO criterionCreateDTO) {
        String name = criterionCreateDTO.name();
        int maxScore = criterionCreateDTO.maxScore();
        UUID segmentId = criterionCreateDTO.segmentId();

        Segment segment = segmentRepository.findById(segmentId).orElseThrow(() -> {
            return new EntityNotFoundException("Segment of id " + segmentId + " not found.", ErrorCode.SEGMENT_NOT_FOUND);
        });

        Criterion criterion = new Criterion(name, maxScore, segment);
        return mapper.toSummaryDTO(criterionRepository.save(criterion));
    }
}
