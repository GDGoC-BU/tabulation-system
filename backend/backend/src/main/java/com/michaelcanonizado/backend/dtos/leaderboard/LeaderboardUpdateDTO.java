package com.michaelcanonizado.backend.dtos.leaderboard;

import com.michaelcanonizado.backend.dtos.formula.FormulaUpdateDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record LeaderboardUpdateDTO(
        @Valid
        @NotNull(message = "required")
        FormulaUpdateDTO formula,

        @NotNull(message = "required")
        @Min(value = 1, message = ">= 1")
        int selectionCount
) {
}
