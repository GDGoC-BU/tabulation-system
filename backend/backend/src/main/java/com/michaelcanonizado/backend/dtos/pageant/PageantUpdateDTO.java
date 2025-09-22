package com.michaelcanonizado.backend.dtos.pageant;

import com.michaelcanonizado.backend.models.PageantStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PageantUpdateDTO(
        @NotBlank(message = "required")
        String title
) {
}
