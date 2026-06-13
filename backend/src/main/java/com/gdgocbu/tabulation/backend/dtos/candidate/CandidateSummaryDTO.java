package com.gdgocbu.tabulation.backend.dtos.candidate;

import com.gdgocbu.tabulation.backend.dtos.college.CollegeSummaryDTO;
import com.gdgocbu.tabulation.backend.models.CandidateGender;

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
