package com.michaelcanonizado.backend.mappers;

import com.michaelcanonizado.backend.dtos.phase.PhaseCreateDTO;
import com.michaelcanonizado.backend.dtos.phase.PhaseDetailedDTO;
import com.michaelcanonizado.backend.dtos.phase.PhaseSummaryDTO;
import com.michaelcanonizado.backend.dtos.phase.PhaseUpdateDTO;
import com.michaelcanonizado.backend.dtos.segment.SegmentUpdateDTO;
import com.michaelcanonizado.backend.models.Phase;
import com.michaelcanonizado.backend.models.Segment;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PhaseMapper {
    Phase toEntity(PhaseCreateDTO phaseCreateDTO);
    PhaseSummaryDTO toSummaryDTO(Phase phase);
    PhaseDetailedDTO toDetailedDTO(Phase phase);

    void updateEntityFromDTO(@MappingTarget Phase phase, PhaseUpdateDTO phaseUpdateDTO);
}
