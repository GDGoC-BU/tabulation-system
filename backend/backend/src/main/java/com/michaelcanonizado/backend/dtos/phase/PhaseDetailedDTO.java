package com.michaelcanonizado.backend.dtos.phase;

import com.michaelcanonizado.backend.dtos.segment.SegmentSummaryDTO;

import java.util.List;
import java.util.UUID;

public record PhaseDetailedDTO(
        UUID id,
        String name,
        int sequence,
        List<SegmentSummaryDTO> segments
) {
}
