package com.michaelcanonizado.backend.dtos.segment;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record SegmentUpdateDTO(
        @NotBlank(message = "required")
        String name
) {
}
