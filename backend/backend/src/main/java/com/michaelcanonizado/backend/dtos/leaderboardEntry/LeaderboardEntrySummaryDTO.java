package com.michaelcanonizado.backend.dtos.leaderboardEntry;

import com.michaelcanonizado.backend.dtos.candidate.CandidateSummaryDTO;
import com.michaelcanonizado.backend.models.CriteriaBreakdown;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record LeaderboardEntrySummaryDTO(
        UUID id,
        CandidateSummaryDTO candidate,
        int rank,
        BigDecimal score,
        Boolean isOverridden,
        String overrideReason,
        boolean isTied,
        boolean isSelected,
        List<CriteriaBreakdown> criteriaBreakdown
) {
}
