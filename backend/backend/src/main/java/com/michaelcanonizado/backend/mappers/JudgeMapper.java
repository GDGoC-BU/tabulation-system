package com.michaelcanonizado.backend.mappers;

import com.michaelcanonizado.backend.dtos.judge.JudgeCreateDTO;
import com.michaelcanonizado.backend.dtos.judge.JudgeSummaryDTO;
import com.michaelcanonizado.backend.models.Judge;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JudgeMapper {
    Judge toEntity(JudgeSummaryDTO judgeSummaryDTO);
    Judge toEntity(JudgeCreateDTO judgeCreateDTO);
    JudgeSummaryDTO toSummaryDTO(Judge judge);
}
