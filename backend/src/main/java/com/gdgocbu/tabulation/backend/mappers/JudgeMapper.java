package com.gdgocbu.tabulation.backend.mappers;

import com.gdgocbu.tabulation.backend.dtos.judge.JudgeBreakdownDTO;
import com.gdgocbu.tabulation.backend.dtos.judge.JudgeCreateDTO;
import com.gdgocbu.tabulation.backend.dtos.judge.JudgeSummaryDTO;
import com.gdgocbu.tabulation.backend.dtos.judge.JudgeUpdateDTO;
import com.gdgocbu.tabulation.backend.models.Judge;
import com.gdgocbu.tabulation.backend.utilities.PasswordEncoderMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = { PasswordEncoderMapper.class })
public interface JudgeMapper {
    @Mapping(target = "passwordHash", source = "password", qualifiedByName = "encodePassword")
    Judge toEntity(JudgeCreateDTO judgeCreateDTO);

    JudgeSummaryDTO toSummaryDTO(Judge judge);
    JudgeBreakdownDTO toBreakdownDTO(Judge judge);

    void updateEntityFromDTO(@MappingTarget Judge judge, JudgeUpdateDTO judgeUpdateDTO);
}
