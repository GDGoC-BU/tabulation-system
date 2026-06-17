package com.gdgocbu.tabulation.backend.mappers;

import com.gdgocbu.tabulation.backend.dtos.formula.FormulaCreateDTO;
import com.gdgocbu.tabulation.backend.dtos.formula.FormulaSummaryDTO;
import com.gdgocbu.tabulation.backend.dtos.formula.FormulaUpdateDTO;
import com.gdgocbu.tabulation.backend.models.Formula;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface FormulaMapper {
    Formula toEntity(FormulaCreateDTO formulaCreateDTO);

    FormulaSummaryDTO toSummaryDTO(Formula formula);

    void updateEntityFromDTO(@MappingTarget Formula formula, FormulaUpdateDTO formulaUpdateDTO);
}
