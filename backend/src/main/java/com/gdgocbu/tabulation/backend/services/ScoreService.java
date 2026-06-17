package com.gdgocbu.tabulation.backend.services;

import com.gdgocbu.tabulation.backend.annotations.RequirePageantStatus;
import com.gdgocbu.tabulation.backend.contexts.PageantContext;
import com.gdgocbu.tabulation.backend.dtos.score.ScoreDetailedDTO;
import com.gdgocbu.tabulation.backend.dtos.score.ScoreUpdateDTO;
import com.gdgocbu.tabulation.backend.exceptions.common.ErrorCode;
import com.gdgocbu.tabulation.backend.exceptions.customs.EntityNotFoundException;
import com.gdgocbu.tabulation.backend.mappers.ScoreMapper;
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
public class ScoreService {
    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private PageantRepository pageantRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private JudgeRepository judgeRepository;

    @Autowired
    private CriterionRepository criterionRepository;

    @Autowired
    private ScoreMapper mapper;

    @Autowired
    private PageantContext pageantContext;

    @RequirePageantStatus({
            PageantStatus.ONGOING,
            PageantStatus.FINALIZING,
            PageantStatus.CLOSED
    })
    public List<ScoreDetailedDTO> getScores(UUID judgeId, UUID candidateId, UUID criterionId, UUID segmentId) {
        UUID selectedPageantId = pageantContext.getId();
        return scoreRepository.findAll(
                Specification.allOf(
                        ScoreSpecification.hasPageant(selectedPageantId),
                        ScoreSpecification.hasJudge(judgeId),
                        ScoreSpecification.hasCandidate(candidateId),
                        ScoreSpecification.hasCriterion(criterionId),
                        ScoreSpecification.hasSegment(segmentId)
                )
        ).stream().map(score -> {
            return mapper.toDetailedDTO(score);
        }).toList();
    }

    @RequirePageantStatus({
            PageantStatus.ONGOING
    })
    @Transactional
    public ScoreDetailedDTO updateScore(UUID id, ScoreUpdateDTO scoreUpdateDTO) {
        Score score = scoreRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Score not found!", ErrorCode.ENTITY_NOT_FOUND);
        });

        pageantContext.assertAccess(score.getJudge().getPageant().getId());

        mapper.updateEntityFromDTO(score, scoreUpdateDTO);
        return mapper.toDetailedDTO(scoreRepository.save(score));
    }

    @Transactional
    public void initializeScores(UUID pageantId) {
        /* Delete all Scores in the pageant before generating to prevent duplicates */
        scoreRepository.deleteByPageantId(pageantId);

        /* THROW PROPER CUSTOM ERROR IN THE isEmpty CHECKS */
        List<Candidate> candidates = candidateRepository.findAllByPageant_Id(pageantId);
        if (candidates.isEmpty()) {
            throw new IllegalStateException("No candidates found for pageantId: " + pageantId);
        }

        List<Judge> judges = judgeRepository.findAllByPageant_Id(pageantId);
        if (judges.isEmpty()) {
            throw new IllegalStateException("No judges found for pageantId: " + pageantId);
        }

        List<Criterion> criteria = criterionRepository.findAll(
                Specification.allOf(
                        CriterionSpecification.hasPageant(pageantId)
                )
        );
        if (criteria.isEmpty()) {
            throw new IllegalStateException("No criteria found for pageantId: " + pageantId);
        }

        List<Score> newScores = new ArrayList<>();

        candidates.forEach(candidate -> {
            criteria.forEach(criterion -> {
                judges.forEach(judge -> {
                    newScores.add(
                            new Score(
                                    0,
                                    judge,
                                    candidate,
                                    criterion
                            )
                    );
                });
            });
        });

        /* Batch save to minimize insert queries */
        scoreRepository.saveAll(newScores);
    }
}
