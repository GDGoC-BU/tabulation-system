package com.michaelcanonizado.backend.dtos.phase;

import com.michaelcanonizado.backend.models.PhaseSegmentStatus;

import java.util.UUID;

public record PhaseSummaryDTO(
        UUID id,
        String name,
        int sequence,
        PhaseSegmentStatus status
) {
}
