package com.gdgocbu.tabulation.backend.dtos.college;

import java.util.UUID;

public record CollegeSummaryDTO(
        UUID id,
        String code,
        String name
) {
}
