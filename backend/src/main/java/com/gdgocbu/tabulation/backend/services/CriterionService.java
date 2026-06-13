package com.gdgocbu.tabulation.backend.services;

import com.gdgocbu.tabulation.backend.annotations.RequirePageantStatus;
import com.gdgocbu.tabulation.backend.contexts.PageantContext;
import com.gdgocbu.tabulation.backend.dtos.criterion.CriterionCreateDTO;
import com.gdgocbu.tabulation.backend.dtos.criterion.CriterionSummaryDTO;
import com.gdgocbu.tabulation.backend.dtos.criterion.CriterionUpdateDTO;
import com.gdgocbu.tabulation.backend.exceptions.common.ErrorCode;
import com.gdgocbu.tabulation.backend.exceptions.customs.EntityNotFoundException;
import com.gdgocbu.tabulation.backend.mappers.CriterionMapper;
import com.gdgocbu.tabulation.backend.models.*;
import com.gdgocbu.tabulation.backend.repositories.*;
import com.gdgocbu.tabulation.backend.specifications.CriterionSpecification;
import com.gdgocbu.tabulation.backend.specifications.ScoreSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    @Autowired
    private PageantContext pageantContext;

    @RequirePageantStatus({
            PageantStatus.PREPARATION
    })
    public CriterionSummaryDTO addCriterion(CriterionCreateDTO criterionCreateDTO) {
        String name = criterionCreateDTO.name();
        int maxScore = criterionCreateDTO.maxScore();
        UUID segmentId = criterionCreateDTO.segmentId();

        Segment segment = segmentRepository.findById(segmentId).orElseThrow(() -> {
            return new EntityNotFoundException("Segment not found!", ErrorCode.ENTITY_NOT_FOUND);
        });

        pageantContext.assertAccess(
                segment.getPhase()
                        .getPageant()
                        .getId()
        );

        Criterion criterion = new Criterion(name, maxScore, segment);
        Criterion savedCriterion = criterionRepository.save(criterion);

        /* Pre-generate the scores for the new criterion */
        List<Candidate> candidates = candidateRepository.findAll();
        List<Judge> judges = judgeRepository.findAll();
        List<Score> newScores = new ArrayList<>();
        candidates.forEach(candidate -> {
            judges.forEach(judge -> {
                newScores.add(new Score(0, judge, candidate, savedCriterion));
            });
        });
        /* Batch save to minimize insert queries */
        scoreRepository.saveAll(newScores);

        return mapper.toSummaryDTO(savedCriterion);
    }

    @RequirePageantStatus({
            PageantStatus.PREPARATION,
            PageantStatus.ONGOING,
            PageantStatus.FINALIZING,
            PageantStatus.CLOSED
    })
    public List<CriterionSummaryDTO> getCriteria() {
        UUID selectedPageantId = pageantContext.getId();
        return criterionRepository.findAll(
                Specification.allOf(
                        CriterionSpecification.hasPageant(selectedPageantId)
                )
        ).stream().map(criterion -> {
            return mapper.toSummaryDTO(criterion);
        }).toList();
    }

    @RequirePageantStatus({
            PageantStatus.PREPARATION,
            PageantStatus.ONGOING
    })
    public CriterionSummaryDTO updateCriterion(UUID id, CriterionUpdateDTO criterionUpdateDTO) {
        Criterion criterion = criterionRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Can't update! Criterion not found.", ErrorCode.ENTITY_NOT_FOUND);
        });

        pageantContext.assertAccess(
                criterion.getSegment()
                        .getPhase()
                        .getPageant()
                        .getId()
        );

        mapper.updateEntityFromDTO(criterion, criterionUpdateDTO);
        return mapper.toSummaryDTO(criterionRepository.save(criterion));
    }

    @RequirePageantStatus({
            PageantStatus.PREPARATION
    })
    @Transactional
    public void deleteCriterion(UUID id) {
        Criterion criterion = criterionRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Can't delete! Criterion not found.", ErrorCode.ENTITY_NOT_FOUND);
        });

        pageantContext.assertAccess(
                criterion.getSegment()
                        .getPhase()
                        .getPageant()
                        .getId()
        );
    }
}
