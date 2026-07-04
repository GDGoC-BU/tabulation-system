package com.gdgocbu.tabulation.backend.dtos.award;

import com.gdgocbu.tabulation.backend.dtos.leaderboard.LeaderboardCreateDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record AwardCreateDTO(
        @NotBlank(message = "required")
        String name,

        @Valid
        LeaderboardCreateDTO qualificationLeaderboard
) {
}
