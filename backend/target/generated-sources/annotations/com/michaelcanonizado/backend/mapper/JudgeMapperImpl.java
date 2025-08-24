package com.michaelcanonizado.backend.mapper;

import com.michaelcanonizado.backend.dtos.judge.JudgeSummaryDTO;
import com.michaelcanonizado.backend.models.Judge;
import java.time.Instant;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-24T20:15:45+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Oracle Corporation)"
)
@Component
public class JudgeMapperImpl implements JudgeMapper {

    @Override
    public Judge toEntity(JudgeSummaryDTO judgeSummaryDTO) {
        if ( judgeSummaryDTO == null ) {
            return null;
        }

        String username = null;

        username = judgeSummaryDTO.username();

        String passwordHash = null;

        Judge judge = new Judge( username, passwordHash );

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
        Instant lastSeenAt = null;
        Instant createdAt = null;
        Instant updatedAt = null;

        id = judge.getId();
        username = judge.getUsername();
        lastSeenAt = judge.getLastSeenAt();
        createdAt = judge.getCreatedAt();
        updatedAt = judge.getUpdatedAt();

        boolean isOnline = false;

        JudgeSummaryDTO judgeSummaryDTO = new JudgeSummaryDTO( id, username, isOnline, lastSeenAt, createdAt, updatedAt );

        return judgeSummaryDTO;
    }
}
