package com.michaelcanonizado.backend.dtos.leaderboard;

import com.michaelcanonizado.backend.dtos.formula.FormulaSummaryDTO;
import com.michaelcanonizado.backend.dtos.leaderboardEntry.LeaderboardEntrySummaryDTO;

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
