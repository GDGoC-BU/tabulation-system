package com.michaelcanonizado.backend.dtos.pageant;

import jakarta.validation.constraints.NotBlank;

public record PageantCreateDTO(
        @NotBlank(message = "required")
        String title
) {
}
