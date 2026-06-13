package com.gdgocbu.tabulation.backend.dtos.leaderboardEntry;

import com.gdgocbu.tabulation.backend.dtos.candidate.CandidateSummaryDTO;
import com.gdgocbu.tabulation.backend.models.CriteriaBreakdown;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record LeaderboardEntrySummaryDTO(
        UUID id,
        CandidateSummaryDTO candidate,
        int rank,
        BigDecimal score,
        boolean isOverridden,
        String overrideReason,
        boolean isTied,
        boolean isSelected,
        List<CriteriaBreakdown> criteriaBreakdown
) {
}
