package com.michaelcanonizado.backend.dtos.candidate;

import com.michaelcanonizado.backend.models.Gender;

import java.time.LocalDateTime;
import java.util.UUID;

public record CandidateSummaryDTO(
        UUID id,
        int number,
        String firstName,
        String lastName,
        Gender gender,
        int age,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
