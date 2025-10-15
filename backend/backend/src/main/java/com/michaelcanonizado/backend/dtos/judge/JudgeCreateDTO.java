package com.michaelcanonizado.backend.dtos.judge;

import com.michaelcanonizado.backend.models.Honorific;
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

        @NotBlank(message = "required")
        String password
) {
}
