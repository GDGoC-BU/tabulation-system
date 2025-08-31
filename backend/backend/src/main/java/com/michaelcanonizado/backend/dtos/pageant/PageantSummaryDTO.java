package com.michaelcanonizado.backend.dtos.pageant;

import com.michaelcanonizado.backend.models.PageantStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record PageantSummaryDTO(
        UUID id,
        String title,
        PageantStatus status,
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
