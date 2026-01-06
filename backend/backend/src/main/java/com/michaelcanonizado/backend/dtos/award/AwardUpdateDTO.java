package com.michaelcanonizado.backend.dtos.award;

import com.michaelcanonizado.backend.models.Formula;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AwardUpdateDTO(
        @NotBlank(message = "required")
        String name,

        @NotNull(message = "required")
        @Min(value = 1, message = ">= 1")
        int candidateLimit,

        @NotBlank(message = "required")
        Formula formula
) {
}
