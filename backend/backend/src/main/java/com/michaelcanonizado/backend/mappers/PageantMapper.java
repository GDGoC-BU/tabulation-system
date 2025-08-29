package com.michaelcanonizado.backend.mappers;

import com.michaelcanonizado.backend.dtos.pageant.PageantCreateDTO;
import com.michaelcanonizado.backend.dtos.pageant.PageantSummaryDTO;
import com.michaelcanonizado.backend.dtos.pageant.PageantUpdateDTO;
import com.michaelcanonizado.backend.models.Pageant;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PageantMapper {
    Pageant toEntity(PageantCreateDTO pageantCreateDTO);
    Pageant toEntity(PageantSummaryDTO pageantSummaryDTO);
    PageantSummaryDTO toSummary(Pageant pageant);

    void updateEntityFromDTO(@MappingTarget Pageant pageant, PageantUpdateDTO pageantUpdateDTO);
}
