package com.michaelcanonizado.backend.services;

import com.michaelcanonizado.backend.annotations.RequirePageantStatus;
import com.michaelcanonizado.backend.contexts.PageantContext;
import com.michaelcanonizado.backend.dtos.phase.PhaseCreateDTO;
import com.michaelcanonizado.backend.dtos.phase.PhaseDetailedDTO;
import com.michaelcanonizado.backend.dtos.phase.PhaseSummaryDTO;
import com.michaelcanonizado.backend.dtos.phase.PhaseUpdateDTO;
import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.customs.EntityNotFoundException;
import com.michaelcanonizado.backend.exceptions.customs.PhaseStatusException;
import com.michaelcanonizado.backend.mappers.PhaseMapper;
import com.michaelcanonizado.backend.models.Pageant;
import com.michaelcanonizado.backend.models.PageantStatus;
import com.michaelcanonizado.backend.models.Phase;
import com.michaelcanonizado.backend.models.PhaseSegmentStatus;
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

    @RequirePageantStatus({
            PageantStatus.PREPARATION,
    })
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

    @RequirePageantStatus({
            PageantStatus.ONGOING
    })
    @Transactional
    public PhaseDetailedDTO startPhase(UUID id) {
        Phase phase = phaseRepository.findById(id).orElseThrow(() -> {
           return new EntityNotFoundException(
                   "Cannot start! Phase not found.",
                   ErrorCode.ENTITY_NOT_FOUND
           );
        });
        pageantContext.assertAccess(phase.getPageant().getId());
        /* TO-IMPLEMENT: Ensure that only 1 has the state ONGOING */
        phase.setStatus(PhaseSegmentStatus.ONGOING);
        return mapper.toDetailedDTO(phaseRepository.save(phase));
    }

    @RequirePageantStatus({
            PageantStatus.ONGOING
    })
    @Transactional
    public PhaseDetailedDTO closePhase(UUID id) {
        Phase phase = phaseRepository.findById(id).orElseThrow(() -> {
           return new EntityNotFoundException(
                   "Cannot close! Phase not found.",
                   ErrorCode.ENTITY_NOT_FOUND
           );
        });
        pageantContext.assertAccess(phase.getPageant().getId());
        phase.setStatus(PhaseSegmentStatus.CLOSED);
        return mapper.toDetailedDTO(phaseRepository.save(phase));
    }

    @RequirePageantStatus({
            PageantStatus.PREPARATION,
            PageantStatus.ONGOING
    })
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

    @RequirePageantStatus({
            PageantStatus.ONGOING
    })
    public PhaseDetailedDTO getOngoingPhase() {
        UUID selectedPageantId = pageantContext.getId();
        List<Phase> phases =  phaseRepository.findAllByPageant_Id(selectedPageantId);

        /* Revisit this. Might want to add a check to verify that only 1 phase should be ongoing */

        Phase ongoingPhase = phaseRepository.findByStatus(PhaseSegmentStatus.ONGOING).orElseThrow(() -> {
            return new EntityNotFoundException(
                    "No ongoing phase for pageant!",
                    ErrorCode.ENTITY_NOT_FOUND
            );
        });

        return mapper.toDetailedDTO(ongoingPhase);
    }

    @RequirePageantStatus({
            PageantStatus.PREPARATION,
            PageantStatus.ONGOING
    })
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

    @RequirePageantStatus({
            PageantStatus.PREPARATION,
    })
    public PhaseDetailedDTO updatePhase(UUID id, PhaseUpdateDTO phaseUpdateDTO) {
        Phase phase = phaseRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Can't update! Phase not found.", ErrorCode.ENTITY_NOT_FOUND);
        });
        pageantContext.assertAccess(phase.getPageant().getId());
        mapper.updateEntityFromDTO(phase, phaseUpdateDTO);
        return mapper.toDetailedDTO(phaseRepository.save(phase));
    }

    @RequirePageantStatus({
            PageantStatus.PREPARATION,
    })
    public void deletePhase(UUID id) {
        Phase phase = phaseRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Can't delete! Phase not found.", ErrorCode.ENTITY_NOT_FOUND);
        });
        pageantContext.assertAccess(phase.getPageant().getId());
        phaseRepository.deleteById(id);
    }

    /* Mapstruct resolver method */
    public Phase findById(UUID id) {
        return phaseRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Phase not found!", ErrorCode.ENTITY_NOT_FOUND);
        });
    }
}
