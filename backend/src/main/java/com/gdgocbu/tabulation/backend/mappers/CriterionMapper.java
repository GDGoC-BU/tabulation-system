package com.gdgocbu.tabulation.backend.mappers;

import com.gdgocbu.tabulation.backend.dtos.criterion.CriterionBreakdownDTO;
import com.gdgocbu.tabulation.backend.dtos.criterion.CriterionHierarchyDTO;
import com.gdgocbu.tabulation.backend.dtos.criterion.CriterionSummaryDTO;
import com.gdgocbu.tabulation.backend.dtos.criterion.CriterionUpdateDTO;
import com.gdgocbu.tabulation.backend.models.Criterion;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CriterionMapper {
    Criterion toEntity(CriterionSummaryDTO criterionSummaryDTO);

    CriterionSummaryDTO toSummaryDTO(Criterion criterion);
    CriterionBreakdownDTO toBreakdownDTO(Criterion criterion);
    CriterionHierarchyDTO toHierarchyDTO(Criterion criterion);

    void updateEntityFromDTO(@MappingTarget Criterion criterion, CriterionUpdateDTO criterionUpdateDTO);
}
