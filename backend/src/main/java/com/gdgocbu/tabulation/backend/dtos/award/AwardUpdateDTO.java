package com.gdgocbu.tabulation.backend.dtos.award;

import com.gdgocbu.tabulation.backend.dtos.leaderboard.LeaderboardUpdateDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record AwardUpdateDTO(
        @NotBlank(message = "required")
        String name,

        @Valid
        LeaderboardUpdateDTO qualificationLeaderboard
) {
}
