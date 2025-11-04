package com.michaelcanonizado.backend.mappers;

import com.michaelcanonizado.backend.dtos.pageant.*;
import com.michaelcanonizado.backend.models.Pageant;
import com.michaelcanonizado.backend.models.PageantStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(
        componentModel = "spring",
        uses = {
                PhaseMapper.class
        }
)
public interface PageantMapper {
    Pageant toEntity(PageantCreateDTO pageantCreateDTO);
    /* Adding SummaryDTO -> Entity will make the enum mapping more complex.
       Just omit it for now to reduce complexity. ANd there won't really be
       a case for this as Summary and Detailed DTOs are response DTOs, they
       won't really be used in requests.

       Pageant toEntity(PageantSummaryDTO pageantSummaryDTO); */
    @Mapping(target = "status", qualifiedByName = "mapStatus")
    PageantSummaryDTO toSummaryDTO(Pageant pageant);

    @Mapping(target = "status", qualifiedByName = "mapStatus")
    PageantHierarchyDTO toHierarchyDTO(Pageant pageant);

    @Mapping(target = "status", qualifiedByName = "mapStatus")
    PageantContextDTO toContextDTO(Pageant pageant);

    void updateEntityFromDTO(@MappingTarget Pageant pageant, PageantUpdateDTO pageantUpdateDTO);

    @Named("mapStatus")
    default PageantStatusDTO mapStatus(PageantStatus status) {
        if (status == null) return null;
        return new PageantStatusDTO(status.name(), status.getColor());
    }
}
