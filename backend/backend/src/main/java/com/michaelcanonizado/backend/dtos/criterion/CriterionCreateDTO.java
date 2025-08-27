package com.michaelcanonizado.backend.dtos.criterion;

import java.util.UUID;

public record CriterionCreateDTO(
        String name,
        int maxScore,
        UUID segmentId
) {
}
