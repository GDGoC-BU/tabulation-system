package com.michaelcanonizado.backend.mappers;

import com.michaelcanonizado.backend.dtos.phase.PhaseCreateDTO;
import com.michaelcanonizado.backend.dtos.phase.PhaseDetailedDTO;
import com.michaelcanonizado.backend.dtos.phase.PhaseSummaryDTO;
import com.michaelcanonizado.backend.models.Phase;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PhaseMapper {
    Phase toEntity(PhaseCreateDTO phaseCreateDTO);
    PhaseSummaryDTO toSummaryDTO(Phase phase);
    PhaseDetailedDTO toDetailedDTO(Phase phase);
}
