package com.michaelcanonizado.backend.mappers;

import com.michaelcanonizado.backend.dtos.formula.FormulaCreateDTO;
import com.michaelcanonizado.backend.dtos.formula.FormulaSummaryDTO;
import com.michaelcanonizado.backend.dtos.formula.FormulaUpdateDTO;
import com.michaelcanonizado.backend.models.Formula;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface FormulaMapper {
    Formula toEntity(FormulaCreateDTO formulaCreateDTO);

    FormulaSummaryDTO toSummaryDTO(Formula formula);

    void updateEntityFromDTO(@MappingTarget Formula formula, FormulaUpdateDTO formulaUpdateDTO);
}
