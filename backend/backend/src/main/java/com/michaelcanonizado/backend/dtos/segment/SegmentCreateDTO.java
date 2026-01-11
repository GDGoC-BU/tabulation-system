package com.michaelcanonizado.backend.dtos.segment;

import com.michaelcanonizado.backend.models.Formula;
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

        @NotNull(message = "required")
        UUID phaseId,

        @NotNull(message = "required")
        boolean hasQualifications,

        @Min(value = 0, message = ">= 0")
        int leaderboardSelectionCount,

        @NotNull(message = "required")
        Formula formula
) {
}
