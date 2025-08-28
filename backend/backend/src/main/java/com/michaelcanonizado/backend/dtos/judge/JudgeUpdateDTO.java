package com.michaelcanonizado.backend.dtos.judge;

import jakarta.validation.constraints.NotBlank;

public record JudgeUpdateDTO(
        @NotBlank(message = "required")
        String username
) {
}
