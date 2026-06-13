package com.gdgocbu.tabulation.backend.dtos.phase;

import com.gdgocbu.tabulation.backend.models.PhaseSegmentStatus;

import java.util.UUID;

public record PhaseSummaryDTO(
        UUID id,
        String name,
        int sequence,
        PhaseSegmentStatus status
) {
}
