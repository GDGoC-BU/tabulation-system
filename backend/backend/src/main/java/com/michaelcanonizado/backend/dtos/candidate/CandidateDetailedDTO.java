package com.michaelcanonizado.backend.dtos.candidate;

import com.michaelcanonizado.backend.dtos.college.CollegeSummaryDTO;
import com.michaelcanonizado.backend.models.CandidateGender;

import java.time.LocalDateTime;
import java.util.UUID;

public record CandidateDetailedDTO(
        UUID id,
        int number,
        String firstName,
        String lastName,
        CandidateGender gender,
        CollegeSummaryDTO college,
        int age,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
