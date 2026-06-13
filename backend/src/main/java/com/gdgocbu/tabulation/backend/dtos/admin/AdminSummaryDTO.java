package com.gdgocbu.tabulation.backend.dtos.admin;

import java.time.LocalDateTime;
import java.util.UUID;

public record AdminSummaryDTO(
        UUID id,
        String username,
        boolean isOnline,
        LocalDateTime lastSeenAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
