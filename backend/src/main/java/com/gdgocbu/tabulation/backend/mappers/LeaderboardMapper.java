package com.gdgocbu.tabulation.backend.mappers;

import com.gdgocbu.tabulation.backend.dtos.leaderboard.LeaderboardCreateDTO;
import com.gdgocbu.tabulation.backend.dtos.leaderboard.LeaderboardDetailedDTO;
import com.gdgocbu.tabulation.backend.dtos.leaderboard.LeaderboardSummaryDTO;
import com.gdgocbu.tabulation.backend.dtos.leaderboard.LeaderboardUpdateDTO;
import com.gdgocbu.tabulation.backend.models.Leaderboard;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(
        componentModel = "spring",
        uses = {
                FormulaMapper.class,
                LeaderboardEntryMapper.class
        }
)
public interface LeaderboardMapper {
    Leaderboard toEntity(LeaderboardCreateDTO leaderboardCreateDTO);

    LeaderboardSummaryDTO toSummaryDTO(Leaderboard leaderboard);

    LeaderboardDetailedDTO toDetailedDTO(Leaderboard leaderboard);

    void updateEntityFromDTO(@MappingTarget Leaderboard leaderboard, LeaderboardUpdateDTO leaderboardUpdateDTO);
}
