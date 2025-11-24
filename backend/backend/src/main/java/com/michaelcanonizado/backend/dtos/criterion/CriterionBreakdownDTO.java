package com.michaelcanonizado.backend.dtos.criterion;

import java.util.UUID;

public record CriterionBreakdownDTO(
        UUID id,
        String name,
        int maxScore
) {
}
