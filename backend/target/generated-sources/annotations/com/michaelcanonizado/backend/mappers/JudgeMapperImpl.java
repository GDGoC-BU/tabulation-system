package com.michaelcanonizado.backend.mappers;

import com.michaelcanonizado.backend.dtos.judge.JudgeCreateDTO;
import com.michaelcanonizado.backend.dtos.judge.JudgeSummaryDTO;
import com.michaelcanonizado.backend.dtos.judge.JudgeUpdateDTO;
import com.michaelcanonizado.backend.models.Judge;
import com.michaelcanonizado.backend.models.Pageant;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-11T17:50:10+0800",
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
        Pageant pageant = null;

        Judge judge = new Judge( username, passwordHash, pageant );

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
        Pageant pageant = null;

        Judge judge = new Judge( username, passwordHash, pageant );

        judge.setLastSeenAt( judgeSummaryDTO.lastSeenAt() );

        return judge;
    }

    @Override
    public JudgeSummaryDTO toSummaryDTO(Judge judge) {
        if ( judge == null ) {
            return null;
        }

        UUID id = null;
        String username = null;
        LocalDateTime lastSeenAt = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        id = judge.getId();
        username = judge.getUsername();
        lastSeenAt = judge.getLastSeenAt();
        createdAt = judge.getCreatedAt();
        updatedAt = judge.getUpdatedAt();

        boolean isOnline = false;

        JudgeSummaryDTO judgeSummaryDTO = new JudgeSummaryDTO( id, username, isOnline, lastSeenAt, createdAt, updatedAt );

        return judgeSummaryDTO;
    }

    @Override
    public void updateEntityFromDTO(Judge judge, JudgeUpdateDTO judgeUpdateDTO) {
        if ( judgeUpdateDTO == null ) {
            return;
        }

        judge.setUsername( judgeUpdateDTO.username() );
    }
}
