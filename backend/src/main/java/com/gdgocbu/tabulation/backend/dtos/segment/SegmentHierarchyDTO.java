package com.gdgocbu.tabulation.backend.dtos.segment;

import com.gdgocbu.tabulation.backend.dtos.criterion.CriterionHierarchyDTO;
import com.gdgocbu.tabulation.backend.dtos.leaderboard.LeaderboardSummaryDTO;
import com.gdgocbu.tabulation.backend.models.PhaseSegmentStatus;

import java.util.List;
import java.util.UUID;

public record SegmentHierarchyDTO(
        UUID id,
        String name,
        int sequence,
        PhaseSegmentStatus status,
        List<CriterionHierarchyDTO> criteria,
        LeaderboardSummaryDTO qualificationLeaderboard
) {
}
