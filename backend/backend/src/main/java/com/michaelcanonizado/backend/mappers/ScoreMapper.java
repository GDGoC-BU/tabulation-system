package com.michaelcanonizado.backend.mappers;

import com.michaelcanonizado.backend.dtos.score.ScoreDetailedDTO;
import com.michaelcanonizado.backend.dtos.score.ScoreUpdateDTO;
import com.michaelcanonizado.backend.models.Score;
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

    void updateEntityFromDTO(@MappingTarget Score score, ScoreUpdateDTO scoreUpdateDTO);
}
