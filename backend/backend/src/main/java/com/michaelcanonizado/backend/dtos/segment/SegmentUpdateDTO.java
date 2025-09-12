package com.michaelcanonizado.backend.dtos.segment;

import com.michaelcanonizado.backend.models.PhaseSegmentStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SegmentUpdateDTO(
        @NotBlank(message = "required")
        String name,

        @NotNull(message = "required")
        @Min(value = 1, message = ">= 1")
        int sequence,

        @NotNull(message = "required")
        PhaseSegmentStatus status
) {
}
