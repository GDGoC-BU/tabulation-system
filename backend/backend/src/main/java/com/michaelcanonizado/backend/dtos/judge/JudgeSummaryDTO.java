package com.michaelcanonizado.backend.dtos.judge;

import com.michaelcanonizado.backend.models.Honorific;

import java.time.LocalDateTime;
import java.util.UUID;

public record JudgeSummaryDTO(
        UUID id,
        String username,
        String firstName,
        String lastName,
        Honorific honorific,
        boolean isOnline,
        LocalDateTime lastSeenAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
