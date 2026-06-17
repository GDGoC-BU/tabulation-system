package com.gdgocbu.tabulation.backend.dtos.score;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ScoreCreateDTO(
        @NotNull(message = "required")
        @Min(value = 0, message = ">= 0")
        int value,

        @NotNull(message = "required")
        UUID judgeId,

        @NotNull(message = "required")
        UUID candidateId,

        @NotNull(message = "required")
        UUID criterionId
) {
}
