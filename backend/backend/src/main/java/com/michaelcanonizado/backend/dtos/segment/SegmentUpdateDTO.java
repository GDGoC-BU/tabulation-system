package com.michaelcanonizado.backend.dtos.segment;

import com.michaelcanonizado.backend.models.Formula;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SegmentUpdateDTO(
        @NotBlank(message = "required")
        String name,

        @Min(value = 1, message = ">= 1")
        Integer candidateLimit,

        Formula formula
) {
}
