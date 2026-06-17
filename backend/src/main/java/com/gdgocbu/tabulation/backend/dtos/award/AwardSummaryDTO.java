package com.gdgocbu.tabulation.backend.dtos.award;

import com.gdgocbu.tabulation.backend.models.Formula;

import java.util.UUID;

public record AwardSummaryDTO(
        UUID id,
        String name,
        int candidateLimit,
        Formula formula
) {
}
