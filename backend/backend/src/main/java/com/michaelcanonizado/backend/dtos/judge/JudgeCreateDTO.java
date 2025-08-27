package com.michaelcanonizado.backend.dtos.judge;

import java.time.Instant;
import java.util.UUID;

public record JudgeCreateDTO(UUID id, String username, boolean isOnline, Instant lastSeenAt, Instant createdAt, Instant updatedAt) {
}
