package com.michaelcanonizado.backend.mappers;

import com.michaelcanonizado.backend.dtos.criterion.CriterionSummaryDTO;
import com.michaelcanonizado.backend.dtos.score.ScoreDetailedDTO;
import com.michaelcanonizado.backend.dtos.score.ScoreUpdateDTO;
import com.michaelcanonizado.backend.models.Candidate;
import com.michaelcanonizado.backend.models.Criterion;
import com.michaelcanonizado.backend.models.Judge;
import com.michaelcanonizado.backend.models.Score;
import com.michaelcanonizado.backend.models.Segment;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-22T22:53:28+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Oracle Corporation)"
)
@Component
public class ScoreMapperImpl implements ScoreMapper {

    @Autowired
    private CriterionMapper criterionMapper;

    @Override
    public ScoreDetailedDTO toDetailedDTO(Score score) {
        if ( score == null ) {
            return null;
        }

        UUID judgeId = null;
        UUID candidateId = null;
        CriterionSummaryDTO criterion = null;
        UUID segmentId = null;
        UUID id = null;
        int value = 0;

        judgeId = scoreJudgeId( score );
        candidateId = scoreCandidateId( score );
        criterion = criterionMapper.toSummaryDTO( score.getCriterion() );
        segmentId = scoreCriterionSegmentId( score );
        id = score.getId();
        value = score.getValue();

        ScoreDetailedDTO scoreDetailedDTO = new ScoreDetailedDTO( id, value, judgeId, candidateId, segmentId, criterion );

        return scoreDetailedDTO;
    }

    @Override
    public void updateEntityFromDTO(Score score, ScoreUpdateDTO scoreUpdateDTO) {
        if ( scoreUpdateDTO == null ) {
            return;
        }

        score.setValue( scoreUpdateDTO.value() );
    }

    private UUID scoreJudgeId(Score score) {
        Judge judge = score.getJudge();
        if ( judge == null ) {
            return null;
        }
        return judge.getId();
    }

    private UUID scoreCandidateId(Score score) {
        Candidate candidate = score.getCandidate();
        if ( candidate == null ) {
            return null;
        }
        return candidate.getId();
    }

    private UUID scoreCriterionSegmentId(Score score) {
        Criterion criterion = score.getCriterion();
        if ( criterion == null ) {
            return null;
        }
        Segment segment = criterion.getSegment();
        if ( segment == null ) {
            return null;
        }
        return segment.getId();
    }
}
