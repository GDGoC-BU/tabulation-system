package com.michaelcanonizado.backend.dtos.leaderboard;

import com.michaelcanonizado.backend.dtos.formula.FormulaSummaryDTO;

import java.time.LocalDateTime;

public record LeaderboardSummaryDTO(
        FormulaSummaryDTO formula,
        int selectionCount,
        LocalDateTime lastCalculatedAt
) {
}
