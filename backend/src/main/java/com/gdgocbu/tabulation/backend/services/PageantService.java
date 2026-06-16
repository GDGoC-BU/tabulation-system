package com.gdgocbu.tabulation.backend.services;

import com.gdgocbu.tabulation.backend.annotations.RequirePageantStatus;
import com.gdgocbu.tabulation.backend.dtos.pageant.PageantCreateDTO;
import com.gdgocbu.tabulation.backend.dtos.pageant.PageantHierarchyDTO;
import com.gdgocbu.tabulation.backend.dtos.pageant.PageantSummaryDTO;
import com.gdgocbu.tabulation.backend.dtos.pageant.PageantUpdateDTO;
import com.gdgocbu.tabulation.backend.exceptions.common.ErrorCode;
import com.gdgocbu.tabulation.backend.exceptions.customs.EntityNotFoundException;
import com.gdgocbu.tabulation.backend.exceptions.customs.PageantAccessDeniedException;
import com.gdgocbu.tabulation.backend.mappers.PageantMapper;
import com.gdgocbu.tabulation.backend.models.*;
import com.gdgocbu.tabulation.backend.repositories.*;
import com.gdgocbu.tabulation.backend.specifications.ScoreSpecification;
import com.gdgocbu.tabulation.backend.utilities.CacheKeyBuilder;
import com.gdgocbu.tabulation.backend.utilities.CacheNameConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PageantService {
    @Autowired
    private PageantRepository pageantRepository;

    @Autowired
    private PhaseRepository phaseRepository;

    @Autowired
    private SegmentRepository segmentRepository;

    @Autowired
    private AwardRepository awardRepository;

    @Autowired
    private AwardLeaderboardRepository awardLeaderboardRepository;

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private PageantMapper mapper;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private CacheKeyBuilder cacheKeyBuilder;

    @Autowired
    private SegmentService segmentService;

    @Autowired
    private ScoreService scoreService;

    public PageantSummaryDTO addPageant(PageantCreateDTO pageantCreateDTO) {
        Pageant savedPageant = pageantRepository.save(mapper.toEntity(pageantCreateDTO));
        PageantSummaryDTO responseDTO = mapper.toSummaryDTO(savedPageant);

        cacheService.put(
                CacheNameConstants.TABULATION,
                cacheKeyBuilder.build("pageants", responseDTO.id()),
                responseDTO
        );
        cacheService.put(
                CacheNameConstants.TABULATION,
                cacheKeyBuilder.build("pageants", responseDTO.id(), "hierarchy"),
                mapper.toHierarchyDTO(savedPageant)
        );
        cacheService.put(
                CacheNameConstants.TABULATION,
                cacheKeyBuilder.build("pageants", responseDTO.id(), "context"),
                mapper.toContextDTO(savedPageant)
        );
        cacheService.evict(
                CacheNameConstants.TABULATION,
                cacheKeyBuilder.build("pageants", "list", "all")
        );
        return responseDTO;
    }

    @RequirePageantStatus({
            PageantStatus.PREPARATION
    })
    public PageantSummaryDTO startPageant(UUID id) {
        Pageant pageant = pageantRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException(
                    "Pageant not found!",
                    ErrorCode.ENTITY_NOT_FOUND
            );
        });

        pageant.setStatus(PageantStatus.ONGOING);
        pageant.setStartedAt(LocalDateTime.now());

        /* Call initializers */
        segmentService.initializeSegments(id);
        scoreService.initializeScores(id);

        Pageant savedPageant = pageantRepository.save(pageant);
        PageantSummaryDTO responseDTO = mapper.toSummaryDTO(savedPageant);

        cacheService.put(
                CacheNameConstants.TABULATION,
                cacheKeyBuilder.build("pageants", id),
                responseDTO
        );
        cacheService.put(
                CacheNameConstants.TABULATION,
                cacheKeyBuilder.build("pageants", id, "hierarchy"),
                mapper.toHierarchyDTO(savedPageant)
        );
        cacheService.put(
                CacheNameConstants.TABULATION,
                cacheKeyBuilder.build("pageants", id, "context"),
                mapper.toContextDTO(savedPageant)
        );
        cacheService.evict(
                CacheNameConstants.TABULATION,
                cacheKeyBuilder.build("pageants", "list", "all")
        );

        return responseDTO;
    }

    @RequirePageantStatus({
            PageantStatus.ONGOING
    })
    public PageantSummaryDTO finalizePageant(UUID id) {
        Pageant pageant = pageantRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Pageant not found!", ErrorCode.ENTITY_NOT_FOUND);
        });

        pageant.setStatus(PageantStatus.FINALIZING);
        Pageant savedPageant = pageantRepository.save(pageant);
        PageantSummaryDTO responseDTO = mapper.toSummaryDTO(savedPageant);

        cacheService.put(
                CacheNameConstants.TABULATION,
                cacheKeyBuilder.build("pageants", id),
                responseDTO
        );
        cacheService.put(
                CacheNameConstants.TABULATION,
                cacheKeyBuilder.build("pageants", id, "hierarchy"),
                mapper.toHierarchyDTO(savedPageant)
        );
        cacheService.put(
                CacheNameConstants.TABULATION,
                cacheKeyBuilder.build("pageants", id, "context"),
                mapper.toContextDTO(savedPageant)
        );
        cacheService.evict(
                CacheNameConstants.TABULATION,
                cacheKeyBuilder.build("pageants", "list", "all")
        );

        return responseDTO;
    }

    @RequirePageantStatus({
            PageantStatus.FINALIZING
    })
    public PageantSummaryDTO closePageant(UUID id) {
        Pageant pageant = pageantRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Pageant not found!", ErrorCode.ENTITY_NOT_FOUND);
        });

        pageant.setStatus(PageantStatus.CLOSED);
        pageant.setEndedAt(LocalDateTime.now());

        Pageant savedPageant = pageantRepository.save(pageant);
        PageantSummaryDTO responseDTO = mapper.toSummaryDTO(savedPageant);

        cacheService.put(
                CacheNameConstants.TABULATION,
                cacheKeyBuilder.build("pageants", id),
                responseDTO
        );
        cacheService.put(
                CacheNameConstants.TABULATION,
                cacheKeyBuilder.build("pageants", id, "hierarchy"),
                mapper.toHierarchyDTO(savedPageant)
        );
        cacheService.put(
                CacheNameConstants.TABULATION,
                cacheKeyBuilder.build("pageants", id, "context"),
                mapper.toContextDTO(savedPageant)
        );
        cacheService.evict(
                CacheNameConstants.TABULATION,
                cacheKeyBuilder.build("pageants", "list", "all")
        );

        return responseDTO;
    }

    public PageantSummaryDTO getPageant(UUID id) {
        String CACHE_NAME = CacheNameConstants.TABULATION;
        String CACHE_KEY = cacheKeyBuilder.build("pageants", id);

        PageantSummaryDTO responseDTO = cacheService.get(
                CACHE_NAME,
                CACHE_KEY,
                PageantSummaryDTO.class
        );

        if (responseDTO == null) {
            responseDTO = mapper.toSummaryDTO(
                    pageantRepository.findById(id).orElseThrow(() -> {
                        return new EntityNotFoundException(
                                "Pageant not found!",
                                ErrorCode.ENTITY_NOT_FOUND
                        );
                    })
            );

            cacheService.put(
                    CACHE_NAME,
                    CACHE_KEY,
                    responseDTO
            );
        }

        return responseDTO;
    }

    @Transactional
    public PageantHierarchyDTO getPageantHierarchy(UUID id) {
        String CACHE_NAME = CacheNameConstants.TABULATION;
        String CACHE_KEY = cacheKeyBuilder.build("pageants", id, "hierarchy");

        PageantHierarchyDTO responseDTO = cacheService.get(
                CACHE_NAME,
                CACHE_KEY,
                PageantHierarchyDTO.class
        );

        if (responseDTO == null) {
            responseDTO = mapper.toHierarchyDTO(
                    pageantRepository.findById(id).orElseThrow(() -> {
                        return new EntityNotFoundException(
                                "Pageant not found!",
                                ErrorCode.ENTITY_NOT_FOUND
                        );
                    })
            );

            cacheService.put(
                    CACHE_NAME,
                    CACHE_KEY,
                    responseDTO
            );
        }

        return responseDTO;
    }

    public List<PageantSummaryDTO> getPageants() {
        String CACHE_NAME = CacheNameConstants.TABULATION;
        String CACHE_KEY = cacheKeyBuilder.build("pageants", "list", "all");

        List<PageantSummaryDTO> responseDTO = cacheService.get(
                CACHE_NAME,
                CACHE_KEY,
                List.class
        );

        if (responseDTO == null) {
            responseDTO = pageantRepository.findAll().stream()
                    .map(pageant -> {
                        return mapper.toSummaryDTO(pageant);
                    }).toList();

            cacheService.put(
                    CACHE_NAME,
                    CACHE_KEY,
                    responseDTO
            );
        }

        return responseDTO;
    }

    public PageantSummaryDTO updatePageant(UUID id, PageantUpdateDTO pageantUpdateDTO) {
        Pageant pageant = pageantRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException(
                    "Can't update! Pageant not found.",
                    ErrorCode.ENTITY_NOT_FOUND
            );
        });

        /* Manually do the status check. Don't use @RequirePageantStatus().

           NOTE: This depends on how the frontend handled pageant delete. Pageant-Id
           is only available in the request headers when the admin selects a pageant,
           but the delete button could be shown in the table itself (no pageant is
           selected here). */
        if (
            pageant.getStatus() != PageantStatus.PREPARATION &&
            pageant.getStatus() != PageantStatus.CLOSED
        ) {
            throw new PageantAccessDeniedException(
                    "Can't update! A pageant can only be updated before or after starting.",
                    ErrorCode.PAGEANT_ACCESS_DENIED
            );
        }

        mapper.updateEntityFromDTO(pageant, pageantUpdateDTO);
        Pageant savedPageant = pageantRepository.save(pageant);
        PageantSummaryDTO responseDTO = mapper.toSummaryDTO(savedPageant);

        cacheService.put(
                CacheNameConstants.TABULATION,
                cacheKeyBuilder.build("pageants", id),
                responseDTO
        );
        cacheService.put(
                CacheNameConstants.TABULATION,
                cacheKeyBuilder.build("pageants", id, "hierarchy"),
                mapper.toHierarchyDTO(savedPageant)
        );
        cacheService.put(
                CacheNameConstants.TABULATION,
                cacheKeyBuilder.build("pageants", id, "context"),
                mapper.toContextDTO(savedPageant)
        );
        cacheService.evict(
                CacheNameConstants.TABULATION,
                cacheKeyBuilder.build("pageants", "list", "all")
        );

        return responseDTO;
    }

    @Transactional
    @CacheEvict(value = CacheNameConstants.TABULATION, allEntries = true)
    public PageantSummaryDTO softResetPageant(UUID id) {
        /* Get the Pageant */
        Pageant pageant = pageantRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Can't reset! Pageant not found.", ErrorCode.ENTITY_NOT_FOUND);
        });

        /* Reset the Leaderboard for each of its Awards */
        List<Award> awards =  awardRepository.findAllByPageant_Id(id);
        List<AwardLeaderboard> leaderboardsForAllAwards = awards
                .stream()
                .flatMap(award -> award.getLeaderboard().stream())
                .toList();

        leaderboardsForAllAwards.forEach(leaderboard -> {
            leaderboard.setScore(BigDecimal.ZERO);
            leaderboard.setCriteriaBreakdown(null);
        });
        awardLeaderboardRepository.saveAll(leaderboardsForAllAwards);

        /* Reset its Scores */
        List<Score> scores = scoreRepository.findAll(
                Specification.allOf(ScoreSpecification.hasPageant(id))
        );
        scores.forEach(score -> score.setValue(0));
        scoreRepository.saveAll(scores);

        /* Reset Phase and Segment Statuses */
        List<Phase> phases = phaseRepository.findAllByPageant_Id(id);
        List<Segment> segments = phases
                .stream()
                .flatMap(phase -> phase.getSegments().stream())
                .toList();

        segments.forEach(segment -> segment.setStatus(PhaseSegmentStatus.PENDING));
        phases.forEach(phase -> phase.setStatus(PhaseSegmentStatus.PENDING));
        segmentRepository.saveAll(segments);
        phaseRepository.saveAll(phases);

        /* RESET LEADERBOARDS */

        /* Reset Pageant status */
        pageant.setStatus(PageantStatus.PREPARATION);

        /* Reset Pageant startedAt and endedAt */
        pageant.setStartedAt(null);
        pageant.setEndedAt(null);

        Pageant savedPageant = pageantRepository.save(pageant);
        return mapper.toSummaryDTO(savedPageant);
    }

    public void deletePageant(UUID id) {
        Pageant pageant = pageantRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Can't delete! Pageant not found.", ErrorCode.ENTITY_NOT_FOUND);
        });

        if (
            pageant.getStatus() != PageantStatus.PREPARATION &&
            pageant.getStatus() != PageantStatus.CLOSED
        ) {
            throw new PageantAccessDeniedException(
                    "Can't delete! A pageant can only be deleted before or after starting.",
                    ErrorCode.PAGEANT_ACCESS_DENIED
            );
        }

        cacheService.evict(
                CacheNameConstants.TABULATION,
                cacheKeyBuilder.build("pageants", id)
        );
        cacheService.evict(
                CacheNameConstants.TABULATION,
                cacheKeyBuilder.build("pageants", id, "hierarchy")
        );
        cacheService.evict(
                CacheNameConstants.TABULATION,
                cacheKeyBuilder.build("pageants", id, "context")
        );
        cacheService.evict(
                CacheNameConstants.TABULATION,
                cacheKeyBuilder.build("pageants", "list", "all")
        );

        pageantRepository.deleteById(id);
    }
}
