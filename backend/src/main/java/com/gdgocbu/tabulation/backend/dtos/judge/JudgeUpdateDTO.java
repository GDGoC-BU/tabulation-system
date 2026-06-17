package com.gdgocbu.tabulation.backend.dtos.judge;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record JudgeUpdateDTO(
        @NotBlank(message = "required")
        String username,

        @NotNull(message = "required")
        @Min(value = 1, message = ">= 1")
        int number
) {
}
