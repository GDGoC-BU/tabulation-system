package com.michaelcanonizado.backend.mappers;

import com.michaelcanonizado.backend.dtos.award.AwardCreateDTO;
import com.michaelcanonizado.backend.dtos.award.AwardDetailedDTO;
import com.michaelcanonizado.backend.dtos.award.AwardSummaryDTO;
import com.michaelcanonizado.backend.dtos.award.AwardUpdateDTO;
import com.michaelcanonizado.backend.models.Award;
import com.michaelcanonizado.backend.utilities.FormulaEncoder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {FormulaEncoder.class})
public interface AwardMapper {
    @Mapping(target = "formula", source = "formula", qualifiedByName = "encodeFormula")
    public Award toEntity(AwardCreateDTO awardCreateDTO);

    @Mapping(target = "formula", source = "formula", qualifiedByName = "decodeFormula")
    public AwardSummaryDTO toSummaryDTO(Award award);

    @Mapping(target = "formula", source = "formula", qualifiedByName = "decodeFormula")
    @Mapping(target = "leaderboard", source = "leaderboard")
    public AwardDetailedDTO toDetailedDTO(Award award);

    @Mapping(target = "formula", source = "formula", qualifiedByName = "encodeFormula")
    void updateEntityFromDTO(@MappingTarget Award award, AwardUpdateDTO awardUpdateDTO);
}
