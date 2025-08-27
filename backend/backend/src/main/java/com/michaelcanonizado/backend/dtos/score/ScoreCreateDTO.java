package com.michaelcanonizado.backend.dtos.score;

import java.util.UUID;

public record ScoreCreateDTO(
        int value,
        UUID judgeId,
        UUID candidateId,
        UUID criterionId
) {
}
