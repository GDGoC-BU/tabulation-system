package com.gdgocbu.tabulation.backend.dtos.criterion;

import java.util.UUID;

public record CriterionSummaryDTO(
        UUID id,
        String name,
        int maxScore
) {
}
