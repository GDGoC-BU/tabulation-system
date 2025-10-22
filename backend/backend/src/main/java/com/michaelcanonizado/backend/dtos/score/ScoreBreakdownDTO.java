package com.michaelcanonizado.backend.dtos.score;

import com.michaelcanonizado.backend.dtos.judge.JudgeBreakdownDTO;

public record ScoreBreakdownDTO(
        JudgeBreakdownDTO judge,
        int value
) {
}
