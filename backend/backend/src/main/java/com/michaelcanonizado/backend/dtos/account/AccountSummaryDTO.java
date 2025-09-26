package com.michaelcanonizado.backend.dtos.account;

import java.time.LocalDateTime;
import java.util.UUID;

public record AccountSummaryDTO(
        UUID id,
        String username,
        String role,
        boolean isOnline,
        LocalDateTime lastSeenAt
) {
}
