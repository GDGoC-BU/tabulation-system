package com.gdgocbu.tabulation.backend.dtos.award;

import com.gdgocbu.tabulation.backend.dtos.awardLeaderboard.AwardLeaderboardSummaryDTO;
import com.gdgocbu.tabulation.backend.models.Formula;

import java.util.List;
import java.util.UUID;

public record AwardDetailedDTO(
        UUID id,
        String name,
        int candidateLimit,
        Formula formula,
        List<AwardLeaderboardSummaryDTO> leaderboard
) {
}
