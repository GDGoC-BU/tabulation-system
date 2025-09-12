package com.michaelcanonizado.backend.services;

import com.michaelcanonizado.backend.contexts.PageantContext;
import com.michaelcanonizado.backend.dtos.phase.PhaseCreateDTO;
import com.michaelcanonizado.backend.dtos.phase.PhaseDetailedDTO;
import com.michaelcanonizado.backend.dtos.phase.PhaseSummaryDTO;
import com.michaelcanonizado.backend.dtos.phase.PhaseUpdateDTO;
import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.customs.EntityNotFoundException;
import com.michaelcanonizado.backend.mappers.PhaseMapper;
import com.michaelcanonizado.backend.models.Pageant;
import com.michaelcanonizado.backend.models.Phase;
import com.michaelcanonizado.backend.repositories.PageantRepository;
import com.michaelcanonizado.backend.repositories.PhaseRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class PhaseService {
    @Autowired
    private PhaseRepository phaseRepository;

    @Autowired
    private PageantRepository pageantRepository;

    @Autowired
    private PhaseMapper mapper;

    @Autowired
    private PageantContext pageantContext;

    public PhaseDetailedDTO addPhase(PhaseCreateDTO phaseCreateDTO) {
        Phase phase = mapper.toEntity(phaseCreateDTO);

        /* Connect to the selected pageant */
        UUID selectedPageantId = pageantContext.getId();
        Pageant pageant = pageantRepository.findById(selectedPageantId).orElseThrow(() -> {
            return new EntityNotFoundException(
                    "Cannot create phase! Pageant being connected to it doesn't exist.",
                    ErrorCode.ENTITY_NOT_FOUND
            );
        });
        phase.setPageant(pageant);

        return mapper.toDetailedDTO(phaseRepository.save(phase));
    }


    @Transactional
    public PhaseDetailedDTO getPhase(UUID id) {
        Phase phase = phaseRepository.findById(id).orElseThrow(() -> {
           return new EntityNotFoundException(
                   "Phase not found!",
                   ErrorCode.ENTITY_NOT_FOUND
           );
        });

        pageantContext.assertAccess(phase.getPageant().getId());

        return mapper.toDetailedDTO(phase);
    }

    public List<PhaseSummaryDTO> getPhases() {
        UUID selectedPageantId = pageantContext.getId();

        return phaseRepository
                .findAllByPageant_Id(selectedPageantId)
                .stream()
                .map(phase -> {
                    return mapper.toSummaryDTO(phase);
                })
                .toList();
    }

    /* Mapstruct resolver method */
    public Phase findById(UUID id) {
        return phaseRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Phase not found!", ErrorCode.ENTITY_NOT_FOUND);
        });
    }

    public PhaseDetailedDTO updatePhase(UUID id, PhaseUpdateDTO phaseUpdateDTO) {
        Phase phase = phaseRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Can't update! Phase not found.", ErrorCode.ENTITY_NOT_FOUND);
        });

        mapper.updateEntityFromDTO(phase, phaseUpdateDTO);

        return mapper.toDetailedDTO(phaseRepository.save(phase));
    }

    public void deletePhase(UUID id) {
        if(!phaseRepository.existsById(id)) {
            throw new EntityNotFoundException(
                    "Can't delete! Phase not found.",
                    ErrorCode.ENTITY_NOT_FOUND
            );
        }

        phaseRepository.deleteById(id);
    }
}
