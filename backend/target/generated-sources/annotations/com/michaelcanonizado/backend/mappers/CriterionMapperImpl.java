package com.michaelcanonizado.backend.mappers;

import com.michaelcanonizado.backend.dtos.criterion.CriterionSummaryDTO;
import com.michaelcanonizado.backend.dtos.criterion.CriterionUpdateDTO;
import com.michaelcanonizado.backend.models.Criterion;
import com.michaelcanonizado.backend.models.Segment;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-12T19:38:19+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Oracle Corporation)"
)
@Component
public class CriterionMapperImpl implements CriterionMapper {

    @Override
    public Criterion toEntity(CriterionSummaryDTO criterionSummaryDTO) {
        if ( criterionSummaryDTO == null ) {
            return null;
        }

        String name = null;
        int maxScore = 0;

        name = criterionSummaryDTO.name();
        maxScore = criterionSummaryDTO.maxScore();

        Segment segment = null;

        Criterion criterion = new Criterion( name, maxScore, segment );

        return criterion;
    }

    @Override
    public CriterionSummaryDTO toSummaryDTO(Criterion criterion) {
        if ( criterion == null ) {
            return null;
        }

        UUID id = null;
        String name = null;
        int maxScore = 0;

        id = criterion.getId();
        name = criterion.getName();
        maxScore = criterion.getMaxScore();

        CriterionSummaryDTO criterionSummaryDTO = new CriterionSummaryDTO( id, name, maxScore );

        return criterionSummaryDTO;
    }

    @Override
    public void updateEntityFromDTO(Criterion criterion, CriterionUpdateDTO criterionUpdateDTO) {
        if ( criterionUpdateDTO == null ) {
            return;
        }

        criterion.setName( criterionUpdateDTO.name() );
        criterion.setMaxScore( criterionUpdateDTO.maxScore() );
    }
}
