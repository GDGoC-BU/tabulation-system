package com.michaelcanonizado.backend.dtos.manager;

import com.michaelcanonizado.backend.models.ManagerRole;

import java.time.LocalDateTime;
import java.util.UUID;

public record ManagerSummaryDTO(
        UUID id,
        String username,
        boolean isOnline,
        ManagerRole role,
        LocalDateTime lastSeenAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
