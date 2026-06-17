package com.gdgocbu.tabulation.backend.dtos.judge;

import com.gdgocbu.tabulation.backend.models.Honorific;

import java.util.UUID;

public record JudgeBreakdownDTO(
        UUID id,
        String username,
        String firstName,
        String lastName,
        Honorific honorific,
        int number
) {
}
