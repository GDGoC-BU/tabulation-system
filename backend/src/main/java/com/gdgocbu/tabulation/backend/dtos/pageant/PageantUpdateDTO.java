package com.gdgocbu.tabulation.backend.dtos.pageant;

import com.gdgocbu.tabulation.backend.models.PageantStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PageantUpdateDTO(
        @NotBlank(message = "required")
        String title
) {
}
