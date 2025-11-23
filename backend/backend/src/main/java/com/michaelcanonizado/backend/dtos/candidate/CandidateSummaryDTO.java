package com.michaelcanonizado.backend.dtos.candidate;

import com.michaelcanonizado.backend.dtos.college.CollegeSummaryDTO;
import com.michaelcanonizado.backend.models.CandidateGender;

import java.time.LocalDateTime;
import java.util.UUID;

public record CandidateSummaryDTO(
        UUID id,
        int number,
        String firstName,
        String lastName,
        CollegeSummaryDTO college,
        CandidateGender gender,
        int age,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
