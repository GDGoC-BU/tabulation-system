package com.michaelcanonizado.backend.dtos.pageant;

import com.michaelcanonizado.backend.dtos.phase.PhaseHierarchyDTO;

import java.util.List;
import java.util.UUID;

public record PageantHierarchyDTO(
        UUID id,
        String title,
        PageantStatusDTO status,
        List<PhaseHierarchyDTO> phases
) {
}
