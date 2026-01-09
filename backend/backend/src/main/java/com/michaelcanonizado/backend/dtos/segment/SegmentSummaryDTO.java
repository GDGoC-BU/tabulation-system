package com.michaelcanonizado.backend.dtos.segment;

import com.michaelcanonizado.backend.dtos.phase.PhaseSummaryDTO;
import com.michaelcanonizado.backend.models.Formula;
import com.michaelcanonizado.backend.models.PhaseSegmentStatus;

import java.util.UUID;

public record SegmentSummaryDTO(
        UUID id,
        String name,
        int sequence,
        Integer candidateLimit,
        Formula formula,
        PhaseSegmentStatus status,
        /* FIX THIS. PhaseDetailedDTO shows its segments and the segment shows their phase? unnecessary.
           But for now keep it, the frontend depends on this relationship. */
        PhaseSummaryDTO phase
) {
}
