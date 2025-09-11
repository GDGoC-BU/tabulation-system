package com.michaelcanonizado.backend.dtos;

import com.michaelcanonizado.backend.dtos.candidate.CandidateSummaryDTO;

import java.util.UUID;

public record AwardLeaderboardSummaryDTO(
        UUID id,
        CandidateSummaryDTO candidate,
        Double score
) {
}
