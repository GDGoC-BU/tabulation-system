package com.michaelcanonizado.backend.dtos.judge;

import java.time.LocalDateTime;
import java.util.UUID;

public record JudgeDetailedDTO(
        UUID id,
        String username,
        boolean isOnline,
        LocalDateTime lastSeenAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
