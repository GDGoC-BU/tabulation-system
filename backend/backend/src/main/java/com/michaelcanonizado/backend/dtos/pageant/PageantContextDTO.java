package com.michaelcanonizado.backend.dtos.pageant;

import java.util.UUID;

public record PageantContextDTO(
        UUID id,
        String title,
        PageantStatusDTO status
) {
}
