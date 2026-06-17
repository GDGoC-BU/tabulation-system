package com.gdgocbu.tabulation.backend.mappers;

import com.gdgocbu.tabulation.backend.dtos.phase.*;
import com.gdgocbu.tabulation.backend.dtos.segment.SegmentUpdateDTO;
import com.gdgocbu.tabulation.backend.models.Phase;
import com.gdgocbu.tabulation.backend.models.Segment;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(
        componentModel = "spring",
        uses = {
                SegmentMapper.class
        }
)
public interface PhaseMapper {
    Phase toEntity(PhaseCreateDTO phaseCreateDTO);

    PhaseSummaryDTO toSummaryDTO(Phase phase);
    PhaseDetailedDTO toDetailedDTO(Phase phase);
    PhaseBreakdownDTO toBreakdownDTO(Phase phase);
    PhaseHierarchyDTO toHierarchyDTO(Phase phase);

    void updateEntityFromDTO(@MappingTarget Phase phase, PhaseUpdateDTO phaseUpdateDTO);
}
