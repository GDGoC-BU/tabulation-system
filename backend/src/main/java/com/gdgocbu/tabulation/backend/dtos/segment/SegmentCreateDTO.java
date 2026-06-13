package com.gdgocbu.tabulation.backend.dtos.segment;

import com.gdgocbu.tabulation.backend.dtos.leaderboard.LeaderboardCreateDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record SegmentCreateDTO(
        @NotBlank(message = "required")
        String name,

        /* If ordering is handled in a different endpoint, remove this.
        When a segment is created, it can be appended to the next sequence */
        @NotNull(message = "required")
        @Min(value = 1, message = ">= 1")
        int sequence,

        @NotNull(message = "required")
        UUID phaseId,

        @Valid
        LeaderboardCreateDTO qualificationLeaderboard
) {
}
