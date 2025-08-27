package com.michaelcanonizado.backend.mappers;

import com.michaelcanonizado.backend.dtos.score.ScoreSummaryDTO;
import com.michaelcanonizado.backend.dtos.score.ScoreUpdateDTO;
import com.michaelcanonizado.backend.models.Score;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-27T21:37:26+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Oracle Corporation)"
)
@Component
public class ScoreMapperImpl implements ScoreMapper {

    @Override
    public ScoreSummaryDTO toSummaryDTO(Score score) {
        if ( score == null ) {
            return null;
        }

        int value = 0;

        value = score.getValue();

        UUID id = null;

        ScoreSummaryDTO scoreSummaryDTO = new ScoreSummaryDTO( id, value );

        return scoreSummaryDTO;
    }

    @Override
    public void updateEntityFromDTO(Score score, ScoreUpdateDTO scoreUpdateDTO) {
        if ( scoreUpdateDTO == null ) {
            return;
        }

        score.setValue( scoreUpdateDTO.value() );
    }
}
