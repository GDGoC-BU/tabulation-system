package com.michaelcanonizado.backend.dtos.segment;

import com.michaelcanonizado.backend.dtos.criterion.CriterionHierarchyDTO;
import com.michaelcanonizado.backend.models.Formula;
import com.michaelcanonizado.backend.models.PhaseSegmentStatus;

import java.util.List;
import java.util.UUID;

public record SegmentHierarchyDTO(
        UUID id,
        String name,
        int sequence,
        PhaseSegmentStatus status,
        List<CriterionHierarchyDTO> criteria,
        boolean hasQualifications,
        int leaderboardSelectionCount,
        Formula formula
) {
}
