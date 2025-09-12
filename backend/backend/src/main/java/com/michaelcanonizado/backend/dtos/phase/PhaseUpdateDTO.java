package com.michaelcanonizado.backend.dtos.phase;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PhaseUpdateDTO(
        @NotBlank(message = "required")
        String name,

        @NotNull(message = "required")
        @Min(value = 1, message = ">= 1")
        int sequence
) {
}
