package com.michaelcanonizado.backend.dtos.segment;

import jakarta.validation.constraints.NotBlank;

public record SegmentCreateDTO(
        @NotBlank(message = "required")
        String name
) {
}
