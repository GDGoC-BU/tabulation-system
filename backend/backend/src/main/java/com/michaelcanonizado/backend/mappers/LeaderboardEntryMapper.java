package com.michaelcanonizado.backend.mappers;

import com.michaelcanonizado.backend.dtos.leaderboardEntry.LeaderboardEntrySummaryDTO;
import com.michaelcanonizado.backend.models.LeaderboardEntry;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LeaderboardEntryMapper {
    LeaderboardEntrySummaryDTO toSummaryDTO(LeaderboardEntry leaderboardEntry);
}
