package com.gdgocbu.tabulation.backend.dtos.judge;

import com.gdgocbu.tabulation.backend.models.Honorific;

import java.time.LocalDateTime;
import java.util.UUID;

public record JudgeSummaryDTO(
        UUID id,
        String username,
        String firstName,
        String lastName,
        Honorific honorific,
        int number,
        boolean isOnline,
        LocalDateTime lastSeenAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
