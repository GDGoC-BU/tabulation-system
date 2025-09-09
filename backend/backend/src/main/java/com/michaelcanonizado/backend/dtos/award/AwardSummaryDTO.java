package com.michaelcanonizado.backend.dtos.award;

import java.util.UUID;

public record AwardSummaryDTO(
        UUID id,
        String name,
        int candidateLimit,
        String formula
) {
}
