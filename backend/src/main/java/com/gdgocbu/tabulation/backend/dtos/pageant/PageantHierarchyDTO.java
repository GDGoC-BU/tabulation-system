package com.gdgocbu.tabulation.backend.dtos.pageant;

import com.gdgocbu.tabulation.backend.dtos.phase.PhaseHierarchyDTO;

import java.util.List;
import java.util.UUID;

public record PageantHierarchyDTO(
        UUID id,
        String title,
        PageantStatusDTO status,
        List<PhaseHierarchyDTO> phases
) {
}
