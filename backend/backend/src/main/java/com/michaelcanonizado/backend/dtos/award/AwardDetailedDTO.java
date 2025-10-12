package com.michaelcanonizado.backend.dtos.award;

import com.michaelcanonizado.backend.dtos.awardLeaderboard.AwardLeaderboardSummaryDTO;

import java.util.List;
import java.util.UUID;

public record AwardDetailedDTO(
        UUID id,
        String name,
        int candidateLimit,
        String formula,
        List<AwardLeaderboardSummaryDTO> leaderboard
) {
}
