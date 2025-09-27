package com.michaelcanonizado.backend.dtos.segment;

import jakarta.validation.constraints.NotBlank;

public record SegmentUpdateDTO(
        @NotBlank(message = "required")
        String name
) {
}
