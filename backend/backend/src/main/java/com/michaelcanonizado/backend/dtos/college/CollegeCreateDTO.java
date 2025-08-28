package com.michaelcanonizado.backend.dtos.college;

import jakarta.validation.constraints.NotBlank;

public record CollegeCreateDTO(
        @NotBlank(message = "required")
        String code,

        @NotBlank(message = "required")
        String name
) {
}
