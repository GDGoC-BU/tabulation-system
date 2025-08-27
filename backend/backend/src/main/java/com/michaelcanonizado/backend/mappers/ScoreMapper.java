package com.michaelcanonizado.backend.mappers;

import com.michaelcanonizado.backend.dtos.score.ScoreUpdateDTO;
import com.michaelcanonizado.backend.models.Score;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ScoreMapper {
    void updateEntityFromDTO(@MappingTarget Score score, ScoreUpdateDTO scoreUpdateDTO);
}
