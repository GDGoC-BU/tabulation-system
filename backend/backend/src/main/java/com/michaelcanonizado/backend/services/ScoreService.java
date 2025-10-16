package com.michaelcanonizado.backend.services;

import com.michaelcanonizado.backend.annotations.RequirePageantStatus;
import com.michaelcanonizado.backend.contexts.PageantContext;
import com.michaelcanonizado.backend.dtos.score.ScoreDetailedDTO;
import com.michaelcanonizado.backend.dtos.score.ScoreUpdateDTO;
import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.customs.EntityNotFoundException;
import com.michaelcanonizado.backend.mappers.ScoreMapper;
import com.michaelcanonizado.backend.models.*;
import com.michaelcanonizado.backend.repositories.PageantRepository;
import com.michaelcanonizado.backend.repositories.ScoreRepository;
import com.michaelcanonizado.backend.specifications.ScoreSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ScoreService {
    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private PageantRepository pageantRepository;

    @Autowired
    private ScoreMapper mapper;

    @Autowired
    private PageantContext pageantContext;

    @RequirePageantStatus({
            PageantStatus.PREPARATION,
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
}
