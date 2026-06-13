package com.gdgocbu.tabulation.backend.mappers;

import com.gdgocbu.tabulation.backend.dtos.score.ScoreBreakdownDTO;
import com.gdgocbu.tabulation.backend.dtos.score.ScoreDetailedDTO;
import com.gdgocbu.tabulation.backend.dtos.score.ScoreUpdateDTO;
import com.gdgocbu.tabulation.backend.models.Score;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {CriterionMapper.class})
public interface ScoreMapper {
    @Mapping(target = "judgeId", source = "judge.id")
    @Mapping(target = "candidateId", source = "candidate.id")
    @Mapping(target = "criterion", source = "criterion")
    @Mapping(target = "segmentId", source = "criterion.segment.id")
    ScoreDetailedDTO toDetailedDTO(Score score);

    ScoreBreakdownDTO toBreakdownDTO(Score score);

    void updateEntityFromDTO(@MappingTarget Score score, ScoreUpdateDTO scoreUpdateDTO);
}
