package com.michaelcanonizado.backend.services;

import com.michaelcanonizado.backend.dtos.criterion.CriterionCreateDTO;
import com.michaelcanonizado.backend.dtos.criterion.CriterionSummaryDTO;
import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.entity.EntityMismatchException;
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

    public CriterionSummaryDTO updateCriterion(UUID id, CriterionSummaryDTO criterionSummaryDTO) {
        if (!id.equals(criterionSummaryDTO.id())) {
            throw new EntityMismatchException(
                    "Path id " + id + " and Body.id " + criterionSummaryDTO.id() + " doesn't match.",
                    ErrorCode.CRITERION_MISMATCH
            );
        }

        Criterion criterion = criterionRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Criterion of id " + id + " doesn't exist.", ErrorCode.CRITERION_NOT_FOUND);
        });

        mapper.updateEntityFromDTO(criterion, criterionSummaryDTO);
        return mapper.toSummaryDTO(criterionRepository.save(criterion));
    }

    public void deleteCriterion(UUID id) {
        Criterion criterion = criterionRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Deletion failed! Criterion of id " + id + " doesn't exist.", ErrorCode.CRITERION_NOT_FOUND);
        });

        System.out.println("-------------------");
        Segment segment = criterion.getSegment();
        segment.removeCriterion(criterion);
        criterionRepository.delete(criterion);
        System.out.println("-------------------");
    }
}
