package com.michaelcanonizado.backend.dtos.account;

import java.time.LocalDateTime;
import java.util.UUID;

public record AccountSummaryDTO(
        UUID id,
        String username,
        boolean isOnline,
        String accountType,
        LocalDateTime lastSeenAt
) {
}
