package com.michaelcanonizado.backend.dtos.manager;

import com.michaelcanonizado.backend.models.ManagerRole;

import java.time.Instant;
import java.util.UUID;

public record ManagerSummaryDTO(
        UUID id,
        String username,
        boolean isOnline,
        Instant lastSeenAt,
        Instant createdAt,
        Instant updatedAt,
        ManagerRole role
) {
}
