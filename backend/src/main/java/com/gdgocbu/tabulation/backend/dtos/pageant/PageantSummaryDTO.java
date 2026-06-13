package com.gdgocbu.tabulation.backend.dtos.pageant;

import java.time.LocalDateTime;
import java.util.UUID;

public record PageantSummaryDTO(
        UUID id,
        String title,
        PageantStatusDTO status,
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
