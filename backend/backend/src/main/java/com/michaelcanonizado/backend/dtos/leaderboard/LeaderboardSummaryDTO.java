package com.michaelcanonizado.backend.dtos.leaderboard;

import com.michaelcanonizado.backend.dtos.formula.FormulaSummaryDTO;

import java.time.LocalDateTime;
import java.util.UUID;

public record LeaderboardSummaryDTO(
        UUID id,
        FormulaSummaryDTO formula,
        int selectionCount,
        LocalDateTime lastCalculatedAt
) {
}
