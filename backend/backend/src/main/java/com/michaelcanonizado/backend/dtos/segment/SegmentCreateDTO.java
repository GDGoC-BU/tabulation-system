package com.michaelcanonizado.backend.dtos.segment;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record SegmentCreateDTO(
        @NotBlank(message = "required")
        String name,

        @NotNull(message = "required")
        @Min(value = 1, message = ">= 1")
        int sequence,

        @Min(value = 1, message = ">= 1")
        Integer candidateLimit,
        
        String formula,

        @NotNull(message = "required")
        UUID phaseId
) {
}
