package com.michaelcanonizado.backend.services;

import com.michaelcanonizado.backend.annotations.RequirePageantStatus;
import com.michaelcanonizado.backend.dtos.score.ScoreDetailedDTO;
import com.michaelcanonizado.backend.dtos.score.ScoreUpdateDTO;
import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.entity.EntityNotFoundException;
import com.michaelcanonizado.backend.exceptions.entity.SegmentStatusException;
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

    @RequirePageantStatus({
            PageantStatus.PREPARATION,
            PageantStatus.ONGOING
    })
    public List<ScoreDetailedDTO> getScores(UUID judgeId, UUID candidateId, UUID criterionId, UUID segmentId) {
        return scoreRepository.findAll(
                Specification.allOf(
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
        Pageant pageant = pageantRepository.findAll().getFirst();

        Score score = scoreRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Score not found!", ErrorCode.ENTITY_NOT_FOUND);
        });

        Criterion criterion = score.getCriterion();
        if (criterion == null) {
            throw new EntityNotFoundException("Criterion not found!", ErrorCode.ENTITY_NOT_FOUND);
        }

        Segment segment = criterion.getSegment();
        if (segment == null) {
            throw new EntityNotFoundException("Segment not found!", ErrorCode.ENTITY_NOT_FOUND);
        }

        if (segment.getStatus() != SegmentStatus.ACTIVE) {
            throw new SegmentStatusException("Segment is not ACTIVE! Can't update score.", ErrorCode.SEGMENT_NOT_ACTIVE);
        }

        mapper.updateEntityFromDTO(score, scoreUpdateDTO);
        return mapper.toDetailedDTO(scoreRepository.save(score));
    }
}
