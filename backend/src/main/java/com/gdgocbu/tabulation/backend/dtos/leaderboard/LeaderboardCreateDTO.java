package com.gdgocbu.tabulation.backend.dtos.leaderboard;

import com.gdgocbu.tabulation.backend.dtos.formula.FormulaCreateDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record LeaderboardCreateDTO(
        @Valid
        @NotNull(message = "required")
        FormulaCreateDTO formula,

        @NotNull(message = "required")
        @Min(value = 1, message = ">= 1")
        int selectionCount
) {
}
