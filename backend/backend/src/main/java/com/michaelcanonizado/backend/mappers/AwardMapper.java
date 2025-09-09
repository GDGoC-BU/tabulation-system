package com.michaelcanonizado.backend.mappers;

import com.michaelcanonizado.backend.dtos.award.AwardCreateDTO;
import com.michaelcanonizado.backend.dtos.award.AwardSummaryDTO;
import com.michaelcanonizado.backend.models.Award;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AwardMapper {
    public Award toEntity(AwardCreateDTO awardCreateDTO);
    public AwardSummaryDTO toSummaryDTO(Award award);
}
