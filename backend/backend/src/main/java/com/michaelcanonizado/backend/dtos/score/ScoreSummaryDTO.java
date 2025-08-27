package com.michaelcanonizado.backend.dtos.score;

import java.util.UUID;

public record ScoreSummaryDTO(
        UUID id,
        int value
) {
}
