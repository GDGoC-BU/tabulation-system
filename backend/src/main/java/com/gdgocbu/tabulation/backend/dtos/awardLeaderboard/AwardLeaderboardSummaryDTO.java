package com.gdgocbu.tabulation.backend.dtos.awardLeaderboard;

import com.gdgocbu.tabulation.backend.dtos.candidate.CandidateSummaryDTO;
import com.gdgocbu.tabulation.backend.models.CriteriaBreakdown;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record AwardLeaderboardSummaryDTO(
        UUID id,
        CandidateSummaryDTO candidate,
        BigDecimal score,
        List<CriteriaBreakdown> criteriaBreakdown
) {
}
