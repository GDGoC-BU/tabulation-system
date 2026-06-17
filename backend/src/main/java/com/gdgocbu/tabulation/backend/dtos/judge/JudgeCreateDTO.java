package com.gdgocbu.tabulation.backend.dtos.judge;

import com.gdgocbu.tabulation.backend.models.Honorific;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record JudgeCreateDTO(
        @NotBlank(message = "required")
        String username,

        @NotBlank(message = "required")
        String firstName,

        @NotBlank(message = "required")
        String lastName,

        @NotNull(message = "required")
        Honorific honorific,

        @NotNull(message = "required")
        @Min(value = 1, message = ">= 1")
        int number,

        @NotBlank(message = "required")
        String password
) {
}
