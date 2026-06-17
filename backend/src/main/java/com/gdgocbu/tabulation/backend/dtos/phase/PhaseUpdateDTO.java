package com.gdgocbu.tabulation.backend.dtos.phase;

import jakarta.validation.constraints.NotBlank;

public record PhaseUpdateDTO(
        @NotBlank(message = "required")
        String name
) {
}
