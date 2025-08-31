package com.michaelcanonizado.backend.services;

import com.michaelcanonizado.backend.dtos.score.ScoreSummaryDTO;
import com.michaelcanonizado.backend.dtos.score.ScoreUpdateDTO;
import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.entity.EntityNotFoundException;
import com.michaelcanonizado.backend.exceptions.entity.PageantStatusException;
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

    public List<ScoreSummaryDTO> getScores(UUID judgeId, UUID candidateId, UUID criterionId, UUID segmentId) {
        return scoreRepository.findAll(
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

    @Transactional
    public ScoreSummaryDTO updateScore(UUID id, ScoreUpdateDTO scoreUpdateDTO) {
        Pageant pageant = pageantRepository.findSingleton().orElseThrow(() -> {
            return new EntityNotFoundException("A pageant doesn't exist! Create a new one.", ErrorCode.ENTITY_NOT_FOUND);
        });

        if (pageant.getStatus() == PageantStatus.CLOSED) {
            throw new PageantStatusException("Pageant is closed! Wait for admin to open.", ErrorCode.PAGEANT_LOCKED);
        }

        if (pageant.getStatus() == PageantStatus.FINALIZING) {
            throw new PageantStatusException("Pageant is finalizing! Can't modify scores.", ErrorCode.PAGEANT_LOCKED);
        }

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
        return mapper.toSummaryDTO(scoreRepository.save(score));
    }
}
