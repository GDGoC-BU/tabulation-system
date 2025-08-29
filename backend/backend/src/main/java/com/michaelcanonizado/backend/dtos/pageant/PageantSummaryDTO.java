package com.michaelcanonizado.backend.dtos.pageant;

import com.michaelcanonizado.backend.models.PageantStatus;

import java.time.Instant;
import java.util.UUID;

public record PageantSummaryDTO(
        UUID id,
        String title,
        PageantStatus status,
        Instant createdAt,
        Instant updatedAt
) {
}
