package com.michaelcanonizado.backend.dtos.awardLeaderboard;

import com.michaelcanonizado.backend.dtos.candidate.CandidateSummaryDTO;
import com.michaelcanonizado.backend.models.CriteriaBreakdown;

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
