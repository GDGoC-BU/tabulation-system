package com.michaelcanonizado.backend.services;

import com.michaelcanonizado.backend.dtos.score.ScoreSummaryDTO;
import com.michaelcanonizado.backend.dtos.score.ScoreUpdateDTO;
import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.entity.EntityNotFoundException;
import com.michaelcanonizado.backend.mappers.ScoreMapper;
import com.michaelcanonizado.backend.models.Score;
import com.michaelcanonizado.backend.repositories.ScoreRepository;
import com.michaelcanonizado.backend.specifications.ScoreSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ScoreService {
    @Autowired
    private ScoreRepository repository;

    @Autowired
    private ScoreMapper mapper;

    public List<ScoreSummaryDTO> getScores(UUID judgeId, UUID candidateId, UUID criterionId, UUID segmentId) {
        return repository.findAll(
                Specification.allOf(
                        ScoreSpecification.hasJudge(judgeId),
                        ScoreSpecification.hasCandidate(candidateId),
                        ScoreSpecification.hasCriterion(criterionId),
                        ScoreSpecification.hasSegment(segmentId)
                )
        ).stream().map(score -> {
            return mapper.toSummaryDTO(score);
        }).toList();
    }

    public ScoreSummaryDTO updateScore(UUID id, ScoreUpdateDTO scoreUpdateDTO) {
        Score score = repository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Score not found!", ErrorCode.SCORE_NOT_FOUND);
        });

        mapper.updateEntityFromDTO(score, scoreUpdateDTO);
        return mapper.toSummaryDTO(repository.save(score));
    }
}
