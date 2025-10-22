package com.michaelcanonizado.backend.dtos.segment;

import java.util.UUID;

public record SegmentBreakdownDTO(
        UUID id,
        String name
) {
}
