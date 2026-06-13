package com.gdgocbu.tabulation.backend.dtos.segment;

import com.gdgocbu.tabulation.backend.dtos.leaderboard.LeaderboardSummaryDTO;
import com.gdgocbu.tabulation.backend.dtos.phase.PhaseSummaryDTO;
import com.gdgocbu.tabulation.backend.models.PhaseSegmentStatus;

import java.util.UUID;

public record SegmentSummaryDTO(
        UUID id,
        String name,
        int sequence,
        PhaseSegmentStatus status,
        /* FIX THIS. PhaseDetailedDTO shows its segments and the segment shows their phase? unnecessary.
           But for now keep it, the frontend depends on this relationship. */
        PhaseSummaryDTO phase,
        LeaderboardSummaryDTO qualificationLeaderboard
) {
}
