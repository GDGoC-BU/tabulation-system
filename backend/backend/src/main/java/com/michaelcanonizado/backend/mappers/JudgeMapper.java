package com.michaelcanonizado.backend.mappers;

import com.michaelcanonizado.backend.dtos.judge.JudgeCreateDTO;
import com.michaelcanonizado.backend.dtos.judge.JudgeDetailedDTO;
import com.michaelcanonizado.backend.dtos.judge.JudgeSummaryDTO;
import com.michaelcanonizado.backend.dtos.judge.JudgeUpdateDTO;
import com.michaelcanonizado.backend.models.Judge;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface JudgeMapper {
    Judge toEntity(JudgeCreateDTO judgeCreateDTO);
    Judge toEntity(JudgeSummaryDTO judgeSummaryDTO);
    Judge toEntity(JudgeDetailedDTO judgeDetailedDTO);
    JudgeSummaryDTO toSummaryDTO(Judge judge);
    JudgeDetailedDTO toDetailedDTO(Judge judge);

    void updateEntityFromDTO(@MappingTarget Judge judge, JudgeUpdateDTO judgeUpdateDTO);
}
