package com.michaelcanonizado.backend.dtos.segment;

import com.michaelcanonizado.backend.dtos.leaderboard.LeaderboardUpdateDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record SegmentUpdateDTO(
        @NotBlank(message = "required")
        String name,

        @Valid
        LeaderboardUpdateDTO leaderboard
) {
}
