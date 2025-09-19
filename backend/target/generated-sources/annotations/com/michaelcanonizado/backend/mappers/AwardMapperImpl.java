package com.michaelcanonizado.backend.mappers;

import com.michaelcanonizado.backend.dtos.award.AwardCreateDTO;
import com.michaelcanonizado.backend.dtos.award.AwardSummaryDTO;
import com.michaelcanonizado.backend.models.Award;
import com.michaelcanonizado.backend.models.Pageant;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-19T19:10:08+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Oracle Corporation)"
)
@Component
public class AwardMapperImpl implements AwardMapper {

    @Override
    public Award toEntity(AwardCreateDTO awardCreateDTO) {
        if ( awardCreateDTO == null ) {
            return null;
        }

        String name = null;
        int candidateLimit = 0;
        String formula = null;

        name = awardCreateDTO.name();
        candidateLimit = awardCreateDTO.candidateLimit();
        formula = awardCreateDTO.formula();

        Pageant pageant = null;

        Award award = new Award( name, candidateLimit, formula, pageant );

        return award;
    }

    @Override
    public AwardSummaryDTO toSummaryDTO(Award award) {
        if ( award == null ) {
            return null;
        }

        UUID id = null;
        String name = null;
        int candidateLimit = 0;
        String formula = null;

        id = award.getId();
        name = award.getName();
        candidateLimit = award.getCandidateLimit();
        formula = award.getFormula();

        AwardSummaryDTO awardSummaryDTO = new AwardSummaryDTO( id, name, candidateLimit, formula );

        return awardSummaryDTO;
    }
}
