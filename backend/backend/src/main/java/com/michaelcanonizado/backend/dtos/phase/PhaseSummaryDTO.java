package com.michaelcanonizado.backend.dtos.phase;

import java.util.UUID;

public record PhaseSummaryDTO(
        UUID id,
        String name,
        int sequence
) {
}
