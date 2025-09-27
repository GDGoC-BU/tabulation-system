package com.michaelcanonizado.backend.dtos.phase;

import jakarta.validation.constraints.NotBlank;

public record PhaseUpdateDTO(
        @NotBlank(message = "required")
        String name
) {
}
