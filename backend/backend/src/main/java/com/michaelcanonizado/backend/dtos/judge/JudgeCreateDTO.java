package com.michaelcanonizado.backend.dtos.judge;

import jakarta.validation.constraints.NotBlank;

public record JudgeCreateDTO(
        @NotBlank(message = "required")
        String username,

        @NotBlank(message = "required")
        String password
) {
}
