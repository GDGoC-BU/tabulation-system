package com.gdgocbu.tabulation.backend.dtos.award;

import com.gdgocbu.tabulation.backend.dtos.leaderboard.LeaderboardSummaryDTO;

import java.util.UUID;

public record AwardSummaryDTO(
        UUID id,
        String name,
        LeaderboardSummaryDTO qualificationLeaderboard
) {
}
