package com.michaelcanonizado.backend.dtos.segment;

import com.michaelcanonizado.backend.dtos.leaderboard.LeaderboardSummaryDTO;
import com.michaelcanonizado.backend.dtos.phase.PhaseSummaryDTO;
import com.michaelcanonizado.backend.models.PhaseSegmentStatus;

import java.util.UUID;

public record SegmentSummaryDTO(
        UUID id,
        String name,
        int sequence,
        PhaseSegmentStatus status,
        /* FIX THIS. PhaseDetailedDTO shows its segments and the segment shows their phase? unnecessary.
           But for now keep it, the frontend depends on this relationship. */
        PhaseSummaryDTO phase,
        LeaderboardSummaryDTO leaderboard
) {
}
