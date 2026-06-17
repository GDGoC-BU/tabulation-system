package com.gdgocbu.tabulation.backend.dtos.leaderboard;

import com.gdgocbu.tabulation.backend.dtos.formula.FormulaSummaryDTO;

import java.time.LocalDateTime;

public record LeaderboardSummaryDTO(
        FormulaSummaryDTO formula,
        int selectionCount,
        LocalDateTime lastCalculatedAt
) {
}
