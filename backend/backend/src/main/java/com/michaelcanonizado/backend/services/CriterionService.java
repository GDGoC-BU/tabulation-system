package com.michaelcanonizado.backend.services;

import com.michaelcanonizado.backend.dtos.criterion.CriterionCreateDTO;
import com.michaelcanonizado.backend.dtos.criterion.CriterionSummaryDTO;
import com.michaelcanonizado.backend.dtos.criterion.CriterionUpdateDTO;
import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.entity.EntityMismatchException;
import com.michaelcanonizado.backend.exceptions.entity.EntityNotFoundException;
import com.michaelcanonizado.backend.mappers.CriterionMapper;
import com.michaelcanonizado.backend.models.*;
import com.michaelcanonizado.backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class CriterionService {
    @Autowired
    private CriterionRepository criterionRepository;

    @Autowired
    private SegmentRepository segmentRepository;

    @Autowired
    private JudgeRepository judgeRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private ScoreRepository scoreRepository;

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
        Criterion savedCriterion = criterionRepository.save(criterion);

        /* Pre-generate the scores for the new criterion */
        List<Candidate> candidates = candidateRepository.findAll();
        List<Judge> judges = judgeRepository.findAll();
        candidates.forEach(candidate -> {
            judges.forEach(judge -> {
                scoreRepository.save(new Score(0, judge, candidate, savedCriterion));
            });
        });

        return mapper.toSummaryDTO(savedCriterion);
    }

    public CriterionSummaryDTO updateCriterion(UUID id, CriterionUpdateDTO criterionUpdateDTO) {
        Criterion criterion = criterionRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Criterion of id " + id + " doesn't exist.", ErrorCode.CRITERION_NOT_FOUND);
        });

        mapper.updateEntityFromDTO(criterion, criterionUpdateDTO);
        return mapper.toSummaryDTO(criterionRepository.save(criterion));
    }

    @Transactional
    public void deleteCriterion(UUID id) {
        Criterion criterion = criterionRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Deletion failed! Criterion of id " + id + " doesn't exist.", ErrorCode.CRITERION_NOT_FOUND);
        });

        Segment segment = criterion.getSegment();
        segment.removeCriterion(criterion);
        criterionRepository.delete(criterion);
    }
}
