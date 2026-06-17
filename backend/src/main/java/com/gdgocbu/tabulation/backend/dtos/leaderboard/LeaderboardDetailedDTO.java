package com.gdgocbu.tabulation.backend.dtos.leaderboard;

import com.gdgocbu.tabulation.backend.dtos.formula.FormulaSummaryDTO;
import com.gdgocbu.tabulation.backend.dtos.leaderboardEntry.LeaderboardEntrySummaryDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record LeaderboardDetailedDTO(
        UUID id,
        FormulaSummaryDTO formula,
        int selectionCount,
        LocalDateTime lastCalculatedAt,
        List<LeaderboardEntrySummaryDTO> entries
) {
}
