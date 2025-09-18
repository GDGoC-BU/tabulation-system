package com.michaelcanonizado.backend.dtos.account;

import jakarta.validation.constraints.NotBlank;

public record AccountCreateDTO(
        @NotBlank(message = "required")
        String username,

        @NotBlank(message = "required")
        String password
) {
}
