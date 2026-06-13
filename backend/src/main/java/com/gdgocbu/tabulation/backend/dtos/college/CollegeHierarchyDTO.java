package com.gdgocbu.tabulation.backend.dtos.college;

import java.util.UUID;

public record CollegeHierarchyDTO(
        UUID id,
        String code,
        String name
) {
}
