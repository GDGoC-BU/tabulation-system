package com.michaelcanonizado.backend.mappers;

import com.michaelcanonizado.backend.dtos.leaderboard.LeaderboardCreateDTO;
import com.michaelcanonizado.backend.dtos.leaderboard.LeaderboardSummaryDTO;
import com.michaelcanonizado.backend.dtos.leaderboard.LeaderboardUpdateDTO;
import com.michaelcanonizado.backend.models.Leaderboard;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(
        componentModel = "spring",
        uses = {
                FormulaMapper.class
        }
)
public interface LeaderboardMapper {
    Leaderboard toEntity(LeaderboardCreateDTO leaderboardCreateDTO);

    LeaderboardSummaryDTO toSummaryDTO(Leaderboard leaderboard);

    void updateEntityFromDTO(@MappingTarget Leaderboard leaderboard, LeaderboardUpdateDTO leaderboardUpdateDTO);
}
