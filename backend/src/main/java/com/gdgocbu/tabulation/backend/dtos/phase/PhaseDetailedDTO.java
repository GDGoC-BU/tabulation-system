package com.gdgocbu.tabulation.backend.dtos.phase;

import com.gdgocbu.tabulation.backend.dtos.segment.SegmentSummaryDTO;
import com.gdgocbu.tabulation.backend.models.PhaseSegmentStatus;

import java.util.List;
import java.util.UUID;

public record PhaseDetailedDTO(
        UUID id,
        String name,
        int sequence,
        PhaseSegmentStatus status,
        List<SegmentSummaryDTO> segments
) {
}
