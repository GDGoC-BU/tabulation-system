package com.gdgocbu.tabulation.backend.dtos.award;

import com.gdgocbu.tabulation.backend.models.Formula;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AwardUpdateDTO(
        @NotBlank(message = "required")
        String name,

        @NotNull(message = "required")
        @Min(value = 1, message = ">= 1")
        int candidateLimit,

        @NotNull(message = "required")
        Formula formula
) {
}
