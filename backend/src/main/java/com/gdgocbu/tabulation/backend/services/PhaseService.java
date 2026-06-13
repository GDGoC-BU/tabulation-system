package com.gdgocbu.tabulation.backend.services;

import com.gdgocbu.tabulation.backend.annotations.RequirePageantStatus;
import com.gdgocbu.tabulation.backend.contexts.PageantContext;
import com.gdgocbu.tabulation.backend.dtos.phase.PhaseCreateDTO;
import com.gdgocbu.tabulation.backend.dtos.phase.PhaseDetailedDTO;
import com.gdgocbu.tabulation.backend.dtos.phase.PhaseSummaryDTO;
import com.gdgocbu.tabulation.backend.dtos.phase.PhaseUpdateDTO;
import com.gdgocbu.tabulation.backend.exceptions.common.ErrorCode;
import com.gdgocbu.tabulation.backend.exceptions.customs.EntityNotFoundException;
import com.gdgocbu.tabulation.backend.exceptions.customs.PhaseSegmentStatusException;
import com.gdgocbu.tabulation.backend.mappers.PhaseMapper;
import com.gdgocbu.tabulation.backend.models.*;
import com.gdgocbu.tabulation.backend.repositories.PageantRepository;
import com.gdgocbu.tabulation.backend.repositories.PhaseRepository;
import com.gdgocbu.tabulation.backend.utilities.CacheKeyBuilder;
import com.gdgocbu.tabulation.backend.utilities.CacheNameConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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

    /* Lazy inject the mapper to break the circular dependency:
       PhaseService → PhaseMapper → SegmentMapper → PhaseService */
    @Lazy
    @Autowired
    private PhaseMapper mapper;

    @Autowired
    private PageantContext pageantContext;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private CacheKeyBuilder cacheKeyBuilder;

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
        Phase savedPhase = phaseRepository.save(phase);
        PhaseDetailedDTO responseDTO = mapper.toDetailedDTO(savedPhase);

        cacheService.put(
                CacheNameConstants.TABULATION,
                cacheKeyBuilder.build("pageants", selectedPageantId, "phases", responseDTO.id()),
                responseDTO
        );
        cacheService.evict(
                CacheNameConstants.TABULATION,
                cacheKeyBuilder.build("pageants", selectedPageantId, "phases", "list", "all")
        );
        cacheService.evict(
                CacheNameConstants.TABULATION,
                cacheKeyBuilder.build("pageants", selectedPageantId, "hierarchy")
        );

        return responseDTO;
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
        Phase savedPhase = phaseRepository.save(phase);
        PhaseDetailedDTO responseDTO = mapper.toDetailedDTO(savedPhase);

        UUID selectedPageantId = pageantContext.getId();
        cacheService.put(
                CacheNameConstants.TABULATION,
                cacheKeyBuilder.build("pageants", selectedPageantId, "phases", "ongoing"),
                responseDTO
        );
        cacheService.put(
                CacheNameConstants.TABULATION,
                cacheKeyBuilder.build("pageants", selectedPageantId, "phases", id),
                responseDTO
        );
        cacheService.evict(
                CacheNameConstants.TABULATION,
                cacheKeyBuilder.build("pageants", selectedPageantId, "phases", "list", "all")
        );
        cacheService.evict(
                CacheNameConstants.TABULATION,
                cacheKeyBuilder.build("pageants", selectedPageantId, "hierarchy")
        );

        return responseDTO;
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
        Phase savedPhase = phaseRepository.save(phase);
        PhaseDetailedDTO responseDTO = mapper.toDetailedDTO(savedPhase);

        UUID selectedPageantId = pageantContext.getId();
        PhaseDetailedDTO cachedOngoingPhase = cacheService.get(
                CacheNameConstants.TABULATION,
                cacheKeyBuilder.build("pageants", selectedPageantId, "phases", "ongoing"),
                PhaseDetailedDTO.class
        );

        /* Check if the current cached ongoing phase is being closed. If it is, remove it from cache */
        if (cachedOngoingPhase.id().equals(responseDTO.id())) {
            cacheService.evict(
                    CacheNameConstants.TABULATION,
                    cacheKeyBuilder.build("pageants", selectedPageantId, "phases", "ongoing")
            );
        }
        cacheService.put(
                CacheNameConstants.TABULATION,
                cacheKeyBuilder.build("pageants", selectedPageantId, "phases", id),
                responseDTO
        );
        cacheService.evict(
                CacheNameConstants.TABULATION,
                cacheKeyBuilder.build("pageants", selectedPageantId, "phases", "list", "all")
        );
        cacheService.evict(
                CacheNameConstants.TABULATION,
                cacheKeyBuilder.build("pageants", selectedPageantId, "hierarchy")
        );

        return responseDTO;
    }

    @RequirePageantStatus({
            PageantStatus.PREPARATION,
            PageantStatus.ONGOING,
            PageantStatus.FINALIZING,
            PageantStatus.CLOSED
    })
    @Transactional
    public PhaseDetailedDTO getPhase(UUID id) {
        UUID selectedPageantId = pageantContext.getId();
        String CACHE_NAME = CacheNameConstants.TABULATION;
        String CACHE_KEY = cacheKeyBuilder.build("pageants", selectedPageantId, "phases", id);

        PhaseDetailedDTO responseDTO = cacheService.get(
                CACHE_NAME,
                CACHE_KEY,
                PhaseDetailedDTO.class
        );

        if (responseDTO == null) {
            Phase phase = phaseRepository.findById(id).orElseThrow(() -> {
                return new EntityNotFoundException(
                        "Phase not found!",
                        ErrorCode.ENTITY_NOT_FOUND
                );
            });
            pageantContext.assertAccess(phase.getPageant().getId());
            responseDTO = mapper.toDetailedDTO(phase);

            cacheService.put(
                    CACHE_NAME,
                    CACHE_KEY,
                    responseDTO
            );
        }

        return responseDTO;
    }

    @RequirePageantStatus({
            PageantStatus.PREPARATION,
            PageantStatus.ONGOING,
            PageantStatus.FINALIZING,
            PageantStatus.CLOSED
    })
    public PhaseDetailedDTO getOngoingPhase() {
        UUID selectedPageantId = pageantContext.getId();
        String CACHE_NAME = CacheNameConstants.TABULATION;
        String CACHE_KEY = cacheKeyBuilder.build("pageants", selectedPageantId, "phases", "ongoing");

        PhaseDetailedDTO responseDTO = cacheService.get(
                CACHE_NAME,
                CACHE_KEY,
                PhaseDetailedDTO.class
        );

        /* Revisit this. The return of this method could be a DTO(there is an ongoing phase) or a
           null(no ongoing phase). If there is no ongoing phase, the database query always runs. */
        if (responseDTO != null) {
            return responseDTO;
        }

        /* Revisit this. Might want to add a more robust check to verify that only 1 segment should be ongoing */
        List<Phase> ongoingPhases = phaseRepository
                .findAllByStatusAndPageantId(
                        PhaseSegmentStatus.ONGOING,
                        selectedPageantId
                );

        /* Only 1 segment should be ongoing */
        if (ongoingPhases.size() > 1) {
            throw new PhaseSegmentStatusException(
                    "Multiple ongoing phases found for pageant " + selectedPageantId,
                    ErrorCode.PHASE_SEGMENT_ILLEGAL_STATE
            );
        }

        responseDTO = ongoingPhases.stream()
                .findFirst()
                .map(mapper::toDetailedDTO)
                .orElse(null);

        if (responseDTO == null) {
            return null;
        }

        cacheService.put(
                CACHE_NAME,
                CACHE_KEY,
                responseDTO
        );
        return responseDTO;
    }

    @RequirePageantStatus({
            PageantStatus.PREPARATION,
            PageantStatus.ONGOING,
            PageantStatus.FINALIZING,
            PageantStatus.CLOSED
    })
    public List<PhaseSummaryDTO> getPhases() {
        UUID selectedPageantId = pageantContext.getId();
        String CACHE_NAME = CacheNameConstants.TABULATION;
        String CACHE_KEY = cacheKeyBuilder.build("pageants", selectedPageantId, "phases", "list", "all");

        List<PhaseSummaryDTO> responseDTO = cacheService.get(
                CACHE_NAME,
                CACHE_KEY,
                List.class
        );

        if (responseDTO == null) {
            responseDTO = phaseRepository
                    .findAllByPageant_Id(selectedPageantId)
                    .stream()
                    .map(phase -> {
                        return mapper.toSummaryDTO(phase);
                    })
                    .toList();

            cacheService.put(
                    CACHE_NAME,
                    CACHE_KEY,
                    responseDTO
            );
        }

        return responseDTO;
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
        Phase savedPhase = phaseRepository.save(phase);
        PhaseDetailedDTO responseDTO = mapper.toDetailedDTO(savedPhase);

        UUID selectedPageantId = pageantContext.getId();
        cacheService.put(
                CacheNameConstants.TABULATION,
                cacheKeyBuilder.build("pageants", selectedPageantId, "phases", id),
                responseDTO
        );
        cacheService.evict(
                CacheNameConstants.TABULATION,
                cacheKeyBuilder.build("pageants", selectedPageantId, "phases", "list", "all")
        );
        cacheService.evict(
                CacheNameConstants.TABULATION,
                cacheKeyBuilder.build("pageants", selectedPageantId, "hierarchy")
        );

        return responseDTO;
    }

    @RequirePageantStatus({
            PageantStatus.PREPARATION,
    })
    public void deletePhase(UUID id) {
        Phase phase = phaseRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Can't delete! Phase not found.", ErrorCode.ENTITY_NOT_FOUND);
        });
        pageantContext.assertAccess(phase.getPageant().getId());

        UUID selectedPageantId = pageantContext.getId();
        cacheService.evict(
                CacheNameConstants.TABULATION,
                cacheKeyBuilder.build("pageants", selectedPageantId, "phases", id)
        );
        cacheService.evict(
                CacheNameConstants.TABULATION,
                cacheKeyBuilder.build("pageants", selectedPageantId, "phases", "list", "all")
        );
        cacheService.evict(
                CacheNameConstants.TABULATION,
                cacheKeyBuilder.build("pageants", selectedPageantId, "hierarchy")
        );

        phaseRepository.deleteById(id);
    }

    /* Mapstruct resolver method */
    public Phase findById(UUID id) {
        return phaseRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Phase not found!", ErrorCode.ENTITY_NOT_FOUND);
        });
    }
}
