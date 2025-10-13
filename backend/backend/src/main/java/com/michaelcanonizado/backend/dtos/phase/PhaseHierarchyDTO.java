package com.michaelcanonizado.backend.dtos.phase;

import com.michaelcanonizado.backend.dtos.segment.SegmentHierarchyDTO;
import com.michaelcanonizado.backend.models.PhaseSegmentStatus;

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
