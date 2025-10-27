package com.michaelcanonizado.backend.dtos.candidate;

import com.michaelcanonizado.backend.models.CandidateGender;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CandidateCreateDTO(
        @NotNull(message = "required")
        @Min(value = 1, message = ">= 1")
        int number,

        @NotBlank(message = "required")
        String firstName,

        @NotBlank(message = "required")
        String lastName,

        @NotNull(message = "required")
        CandidateGender candidateGender,

        @NotNull(message = "required")
        @Min(value = 0, message = ">= 0")
        int age,

        @NotNull(message = "required")
        UUID collegeId
) {
}
