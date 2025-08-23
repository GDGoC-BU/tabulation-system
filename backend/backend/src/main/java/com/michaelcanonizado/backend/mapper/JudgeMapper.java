package com.michaelcanonizado.backend.mapper;

import com.michaelcanonizado.backend.dtos.judge.JudgeSummaryDTO;
import com.michaelcanonizado.backend.models.Judge;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JudgeMapper {
    Judge toEntity(JudgeSummaryDTO judgeSummaryDTO);
    JudgeSummaryDTO toSummaryDTO(Judge judge);
}
