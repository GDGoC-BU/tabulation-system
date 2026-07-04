package com.gdgocbu.tabulation.backend.mappers;

import com.gdgocbu.tabulation.backend.dtos.award.AwardCreateDTO;
import com.gdgocbu.tabulation.backend.dtos.award.AwardDetailedDTO;
import com.gdgocbu.tabulation.backend.dtos.award.AwardSummaryDTO;
import com.gdgocbu.tabulation.backend.dtos.award.AwardUpdateDTO;
import com.gdgocbu.tabulation.backend.models.Award;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(
        componentModel = "spring",
        uses = {
                LeaderboardMapper.class
        }
)
public interface AwardMapper {
    public Award toEntity(AwardCreateDTO awardCreateDTO);

    public AwardSummaryDTO toSummaryDTO(Award award);

    @Mapping(target = "leaderboard", source = "leaderboard")
    public AwardDetailedDTO toDetailedDTO(Award award);

    void updateEntityFromDTO(@MappingTarget Award award, AwardUpdateDTO awardUpdateDTO);
}
