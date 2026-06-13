package com.gdgocbu.tabulation.backend.dtos.segment;

import com.gdgocbu.tabulation.backend.dtos.criterion.CriterionSummaryDTO;
import com.gdgocbu.tabulation.backend.dtos.leaderboard.LeaderboardSummaryDTO;
import com.gdgocbu.tabulation.backend.dtos.phase.PhaseSummaryDTO;
import com.gdgocbu.tabulation.backend.models.PhaseSegmentStatus;

import java.util.List;
import java.util.UUID;

public record SegmentDetailedDTO(
        UUID id,
        String name,
        int sequence,
        PhaseSegmentStatus status,
        PhaseSummaryDTO phase,
        List<CriterionSummaryDTO> criteria,
        LeaderboardSummaryDTO qualificationLeaderboard
) {
}
