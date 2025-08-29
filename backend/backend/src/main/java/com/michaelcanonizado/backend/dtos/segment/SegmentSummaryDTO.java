package com.michaelcanonizado.backend.dtos.segment;

import com.michaelcanonizado.backend.dtos.criterion.CriterionSummaryDTO;
import com.michaelcanonizado.backend.models.SegmentStatus;

import java.util.List;
import java.util.UUID;

public record SegmentSummaryDTO(
        UUID id,
        String name,
        int phase,
        SegmentStatus status,
        List<CriterionSummaryDTO> criteria
) {
}
