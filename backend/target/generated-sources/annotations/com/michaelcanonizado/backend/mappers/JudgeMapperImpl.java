package com.michaelcanonizado.backend.mappers;

import com.michaelcanonizado.backend.dtos.judge.JudgeCreateDTO;
import com.michaelcanonizado.backend.dtos.judge.JudgeDetailedDTO;
import com.michaelcanonizado.backend.dtos.judge.JudgeSummaryDTO;
import com.michaelcanonizado.backend.dtos.judge.JudgeUpdateDTO;
import com.michaelcanonizado.backend.models.Judge;
import java.time.Instant;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-29T22:27:26+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Oracle Corporation)"
)
@Component
public class JudgeMapperImpl implements JudgeMapper {

    @Override
    public Judge toEntity(JudgeCreateDTO judgeCreateDTO) {
        if ( judgeCreateDTO == null ) {
            return null;
        }

        String username = null;

        username = judgeCreateDTO.username();

        String passwordHash = null;

        Judge judge = new Judge( username, passwordHash );

        return judge;
    }

    @Override
    public Judge toEntity(JudgeSummaryDTO judgeSummaryDTO) {
        if ( judgeSummaryDTO == null ) {
            return null;
        }

        String username = null;

        username = judgeSummaryDTO.username();

        String passwordHash = null;

        Judge judge = new Judge( username, passwordHash );

        return judge;
    }

    @Override
    public Judge toEntity(JudgeDetailedDTO judgeDetailedDTO) {
        if ( judgeDetailedDTO == null ) {
            return null;
        }

        String username = null;

        username = judgeDetailedDTO.username();

        String passwordHash = null;

        Judge judge = new Judge( username, passwordHash );

        judge.setLastSeenAt( judgeDetailedDTO.lastSeenAt() );

        return judge;
    }

    @Override
    public JudgeSummaryDTO toSummaryDTO(Judge judge) {
        if ( judge == null ) {
            return null;
        }

        UUID id = null;
        String username = null;

        id = judge.getId();
        username = judge.getUsername();

        JudgeSummaryDTO judgeSummaryDTO = new JudgeSummaryDTO( id, username );

        return judgeSummaryDTO;
    }

    @Override
    public JudgeDetailedDTO toDetailedDTO(Judge judge) {
        if ( judge == null ) {
            return null;
        }

        UUID id = null;
        String username = null;
        Instant lastSeenAt = null;
        Instant createdAt = null;
        Instant updatedAt = null;

        id = judge.getId();
        username = judge.getUsername();
        lastSeenAt = judge.getLastSeenAt();
        createdAt = judge.getCreatedAt();
        updatedAt = judge.getUpdatedAt();

        boolean isOnline = false;

        JudgeDetailedDTO judgeDetailedDTO = new JudgeDetailedDTO( id, username, isOnline, lastSeenAt, createdAt, updatedAt );

        return judgeDetailedDTO;
    }

    @Override
    public void updateEntityFromDTO(Judge judge, JudgeUpdateDTO judgeUpdateDTO) {
        if ( judgeUpdateDTO == null ) {
            return;
        }

        judge.setUsername( judgeUpdateDTO.username() );
    }
}
