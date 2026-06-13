package com.gdgocbu.tabulation.backend.dtos.segment;

import com.gdgocbu.tabulation.backend.dtos.leaderboard.LeaderboardUpdateDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record SegmentUpdateDTO(
        @NotBlank(message = "required")
        String name,

        @Valid
        LeaderboardUpdateDTO qualificationLeaderboard
) {
}
