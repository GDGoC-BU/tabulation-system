package com.michaelcanonizado.backend.services;

import com.michaelcanonizado.backend.dtos.score.ScoreSummaryDTO;
import com.michaelcanonizado.backend.dtos.score.ScoreUpdateDTO;
import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.entity.EntityNotFoundException;
import com.michaelcanonizado.backend.mappers.ScoreMapper;
import com.michaelcanonizado.backend.models.Score;
import com.michaelcanonizado.backend.repositories.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ScoreService {
    @Autowired
    private ScoreRepository repository;

    @Autowired
    private ScoreMapper mapper;

    public ScoreSummaryDTO updateScore(UUID id, ScoreUpdateDTO scoreUpdateDTO) {
        Score score = repository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Score not found!", ErrorCode.SCORE_NOT_FOUND);
        });

        mapper.updateEntityFromDTO(score, scoreUpdateDTO);
        return mapper.toSummaryDTO(repository.save(score));
    }
}
