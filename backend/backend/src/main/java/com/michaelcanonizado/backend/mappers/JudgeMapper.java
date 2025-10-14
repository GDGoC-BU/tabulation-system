package com.michaelcanonizado.backend.mappers;

import com.michaelcanonizado.backend.dtos.judge.JudgeCreateDTO;
import com.michaelcanonizado.backend.dtos.judge.JudgeSummaryDTO;
import com.michaelcanonizado.backend.dtos.judge.JudgeUpdateDTO;
import com.michaelcanonizado.backend.models.Judge;
import com.michaelcanonizado.backend.utilities.PasswordEncoderMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = { PasswordEncoderMapper.class })
public interface JudgeMapper {
    @Mapping(target = "passwordHash", source = "password", qualifiedByName = "encodePassword")
    Judge toEntity(JudgeCreateDTO judgeCreateDTO);

    JudgeSummaryDTO toSummaryDTO(Judge judge);

    void updateEntityFromDTO(@MappingTarget Judge judge, JudgeUpdateDTO judgeUpdateDTO);
}
