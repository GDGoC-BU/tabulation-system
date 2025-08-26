package com.michaelcanonizado.backend.mappers;

import com.michaelcanonizado.backend.dtos.criterion.CriterionSummaryDTO;
import com.michaelcanonizado.backend.models.Criterion;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CriterionMapper {
    Criterion toEntity(CriterionSummaryDTO criterionSummaryDTO);
    CriterionSummaryDTO toSummaryDTO(Criterion criterion);
}
