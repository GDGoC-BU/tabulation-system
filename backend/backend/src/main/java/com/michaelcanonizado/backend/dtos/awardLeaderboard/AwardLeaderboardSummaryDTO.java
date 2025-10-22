package com.michaelcanonizado.backend.dtos.awardLeaderboard;

import com.michaelcanonizado.backend.dtos.candidate.CandidateSummaryDTO;
import com.michaelcanonizado.backend.models.CriteriaBreakdown;

import java.util.List;
import java.util.UUID;

public record AwardLeaderboardSummaryDTO(
        UUID id,
        CandidateSummaryDTO candidate,
        Double score,
        List<CriteriaBreakdown> criteriaBreakdown
) {
}
