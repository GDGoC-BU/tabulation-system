package com.gdgocbu.tabulation.backend.dtos.score;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ScoreUpdateDTO(
        @NotNull(message = "required")
        @Min(value = 0, message = ">= 0")
        int value
) {
}
