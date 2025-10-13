package com.michaelcanonizado.backend.mappers;

import com.michaelcanonizado.backend.dtos.awardLeaderboard.AwardLeaderboardSummaryDTO;
import com.michaelcanonizado.backend.models.AwardLeaderboard;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AwardLeaderboardMapper {
    AwardLeaderboardSummaryDTO toSummaryDTO(AwardLeaderboard awardLeaderboard);
}
