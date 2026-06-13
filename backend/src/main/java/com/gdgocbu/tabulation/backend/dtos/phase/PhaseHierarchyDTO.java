package com.gdgocbu.tabulation.backend.dtos.phase;

import com.gdgocbu.tabulation.backend.dtos.segment.SegmentHierarchyDTO;
import com.gdgocbu.tabulation.backend.models.PhaseSegmentStatus;

import java.util.List;
import java.util.UUID;

public record PhaseHierarchyDTO(
        UUID id,
        String name,
        int sequence,
        PhaseSegmentStatus status,
        List<SegmentHierarchyDTO> segments
) {
}
