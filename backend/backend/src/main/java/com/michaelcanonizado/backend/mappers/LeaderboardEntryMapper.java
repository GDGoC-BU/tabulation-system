package com.michaelcanonizado.backend.mappers;

import com.michaelcanonizado.backend.dtos.leaderboardEntry.LeaderboardEntrySummaryDTO;
import com.michaelcanonizado.backend.models.LeaderboardEntry;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = {
                CandidateMapper.class
        }
)
public interface LeaderboardEntryMapper {
    @Mapping(target = "isOverridden", source = "overridden")
    @Mapping(target = "isTied", source = "tied")
    @Mapping(target = "isSelected", source = "selected")
    LeaderboardEntrySummaryDTO toSummaryDTO(LeaderboardEntry leaderboardEntry);
}
