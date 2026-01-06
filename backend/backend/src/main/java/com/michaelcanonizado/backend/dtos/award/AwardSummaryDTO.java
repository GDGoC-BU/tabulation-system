package com.michaelcanonizado.backend.dtos.award;

import com.michaelcanonizado.backend.models.Formula;

import java.util.UUID;

public record AwardSummaryDTO(
        UUID id,
        String name,
        int candidateLimit,
        Formula formula
) {
}
