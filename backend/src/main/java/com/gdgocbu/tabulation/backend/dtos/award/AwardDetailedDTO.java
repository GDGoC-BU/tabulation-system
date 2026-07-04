package com.gdgocbu.tabulation.backend.dtos.award;

import com.gdgocbu.tabulation.backend.dtos.leaderboard.LeaderboardSummaryDTO;
import java.util.UUID;

public record AwardDetailedDTO(
        UUID id,
        String name,
        LeaderboardSummaryDTO qualificationLeaderboard
) {
}
