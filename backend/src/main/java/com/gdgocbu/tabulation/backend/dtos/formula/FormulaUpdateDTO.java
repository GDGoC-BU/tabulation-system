package com.gdgocbu.tabulation.backend.dtos.formula;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FormulaUpdateDTO(
        @NotBlank(message = "required")
        String text,

        @NotNull(message = "required")
        JsonNode workspace
) {
}
