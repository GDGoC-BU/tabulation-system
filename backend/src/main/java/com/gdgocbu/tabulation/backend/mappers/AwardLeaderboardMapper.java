package com.gdgocbu.tabulation.backend.mappers;

import com.gdgocbu.tabulation.backend.dtos.awardLeaderboard.AwardLeaderboardSummaryDTO;
import com.gdgocbu.tabulation.backend.models.AwardLeaderboard;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AwardLeaderboardMapper {
    AwardLeaderboardSummaryDTO toSummaryDTO(AwardLeaderboard awardLeaderboard);
}
