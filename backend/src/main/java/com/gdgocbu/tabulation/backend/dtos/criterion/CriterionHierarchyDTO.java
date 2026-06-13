package com.gdgocbu.tabulation.backend.dtos.criterion;

import java.util.UUID;

public record CriterionHierarchyDTO(
        UUID id,
        String name,
        int maxScore
) {
}
