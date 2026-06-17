package com.gdgocbu.tabulation.backend.dtos.score;

import com.gdgocbu.tabulation.backend.dtos.criterion.CriterionSummaryDTO;

import java.util.UUID;

public record ScoreDetailedDTO(
        UUID id,
        int value,
        UUID judgeId,
        UUID candidateId,
        UUID segmentId,
        CriterionSummaryDTO criterion
) {
}
