package com.michaelcanonizado.backend.dtos.award;

import com.michaelcanonizado.backend.dtos.awardLeaderboard.AwardLeaderboardSummaryDTO;
import com.michaelcanonizado.backend.models.Formula;

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
