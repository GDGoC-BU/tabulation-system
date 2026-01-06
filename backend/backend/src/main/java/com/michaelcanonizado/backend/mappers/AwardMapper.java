package com.michaelcanonizado.backend.mappers;

import com.michaelcanonizado.backend.dtos.award.AwardCreateDTO;
import com.michaelcanonizado.backend.dtos.award.AwardDetailedDTO;
import com.michaelcanonizado.backend.dtos.award.AwardSummaryDTO;
import com.michaelcanonizado.backend.dtos.award.AwardUpdateDTO;
import com.michaelcanonizado.backend.models.Award;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AwardMapper {
    public Award toEntity(AwardCreateDTO awardCreateDTO);

    public AwardSummaryDTO toSummaryDTO(Award award);

    @Mapping(target = "leaderboard", source = "leaderboard")
    public AwardDetailedDTO toDetailedDTO(Award award);

    void updateEntityFromDTO(@MappingTarget Award award, AwardUpdateDTO awardUpdateDTO);
}
