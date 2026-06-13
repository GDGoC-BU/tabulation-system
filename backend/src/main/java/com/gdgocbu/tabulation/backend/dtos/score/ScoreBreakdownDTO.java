package com.gdgocbu.tabulation.backend.dtos.score;

import com.gdgocbu.tabulation.backend.dtos.judge.JudgeBreakdownDTO;

public record ScoreBreakdownDTO(
        JudgeBreakdownDTO judge,
        int value
) {
}
