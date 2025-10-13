package com.michaelcanonizado.backend.dtos.award;

import com.michaelcanonizado.backend.models.CandidateGender;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AwardCreateDTO(
        @NotBlank(message = "required")
        String name,

        @NotNull(message = "required")
        @Min(value = 1, message = ">= 1")
        int candidateLimit,

        @NotBlank(message = "required")
        String formula
) {
}
