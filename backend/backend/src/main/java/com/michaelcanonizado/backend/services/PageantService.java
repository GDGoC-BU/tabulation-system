package com.michaelcanonizado.backend.services;

import com.michaelcanonizado.backend.annotations.RequirePageantStatus;
import com.michaelcanonizado.backend.dtos.pageant.PageantCreateDTO;
import com.michaelcanonizado.backend.dtos.pageant.PageantHierarchyDTO;
import com.michaelcanonizado.backend.dtos.pageant.PageantSummaryDTO;
import com.michaelcanonizado.backend.dtos.pageant.PageantUpdateDTO;
import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.customs.EntityNotFoundException;
import com.michaelcanonizado.backend.exceptions.customs.PageantAccessDeniedException;
import com.michaelcanonizado.backend.mappers.PageantMapper;
import com.michaelcanonizado.backend.models.*;
import com.michaelcanonizado.backend.repositories.*;
import com.michaelcanonizado.backend.specifications.CandidateSegmentQualificationSpecification;
import com.michaelcanonizado.backend.specifications.ScoreSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PageantService {
    private static final String CACHE_NAME = "PAGEANT";

    @Autowired
    private PageantRepository pageantRepository;

    @Autowired
    private PhaseRepository phaseRepository;

    @Autowired
    private SegmentRepository segmentRepository;

    @Autowired
    private CandidateSegmentQualificationRepository csqRepository;

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

    @Caching(
            put = @CachePut(value = CACHE_NAME, key = "#result.id()"),
            evict = @CacheEvict(value = CACHE_NAME, key = "'pageants'")
    )
    public PageantSummaryDTO addPageant(PageantCreateDTO pageantCreateDTO) {
        Pageant pageant = pageantRepository.save(mapper.toEntity(pageantCreateDTO));
        return mapper.toSummaryDTO(pageant);
    }

    @Caching(
            put = @CachePut(value = CACHE_NAME, key = "#result.id()"),
            evict = {
                    @CacheEvict(value = CACHE_NAME, key = "#result.id() + '_hierarchy'"),
                    @CacheEvict(value = CACHE_NAME, key = "'pageants'")
            }
    )
    @RequirePageantStatus({
            PageantStatus.PREPARATION
    })
    public PageantSummaryDTO startPageant(UUID id) {
        Pageant pageant = pageantRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Pageant not found!", ErrorCode.ENTITY_NOT_FOUND);
        });

        pageant.setStatus(PageantStatus.ONGOING);
        pageant.setStartedAt(LocalDateTime.now());

        return mapper.toSummaryDTO(pageantRepository.save(pageant));
    }

    @Caching(
            put = @CachePut(value = CACHE_NAME, key = "#result.id()"),
            evict = {
                    @CacheEvict(value = CACHE_NAME, key = "#result.id() + '_hierarchy'"),
                    @CacheEvict(value = CACHE_NAME, key = "'pageants'")
            }
    )
    @RequirePageantStatus({
            PageantStatus.ONGOING
    })
    public PageantSummaryDTO finalizePageant(UUID id) {
        Pageant pageant = pageantRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Pageant not found!", ErrorCode.ENTITY_NOT_FOUND);
        });

        pageant.setStatus(PageantStatus.FINALIZING);

        return mapper.toSummaryDTO(pageantRepository.save(pageant));
    }

    @Caching(
            put = @CachePut(value = CACHE_NAME, key = "#result.id()"),
            evict = {
                    @CacheEvict(value = CACHE_NAME, key = "#result.id() + '_hierarchy'"),
                    @CacheEvict(value = CACHE_NAME, key = "'pageants'")
            }
    )
    @RequirePageantStatus({
            PageantStatus.FINALIZING
    })
    public PageantSummaryDTO closePageant(UUID id) {
        Pageant pageant = pageantRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Pageant not found!", ErrorCode.ENTITY_NOT_FOUND);
        });

        pageant.setStatus(PageantStatus.CLOSED);
        pageant.setEndedAt(LocalDateTime.now());

        return mapper.toSummaryDTO(pageantRepository.save(pageant));
    }

    @Cacheable(value = CACHE_NAME, key = "#id")
    public PageantSummaryDTO getPageant(UUID id) {
        Pageant pageant = pageantRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Pageant not found!", ErrorCode.ENTITY_NOT_FOUND);
        });
        return mapper.toSummaryDTO(pageant);
    }

    @Cacheable(value = CACHE_NAME, key = "#id + '_hierarchy'")
    public PageantHierarchyDTO getPageantHierarchy(UUID id) {
        Pageant pageant = pageantRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Pageant not found!", ErrorCode.ENTITY_NOT_FOUND);
        });

        return mapper.toHierarchyDTO(pageant);
    }

    @Cacheable(value = CACHE_NAME, key = "'pageants'")
    public List<PageantSummaryDTO> getPageants() {
        return pageantRepository.findAll().stream()
                .map(pageant -> {
                    return mapper.toSummaryDTO(pageant);
                }).toList();
    }

    @Caching(
            put = @CachePut(value = CACHE_NAME, key = "#result.id()"),
            evict = {
                    @CacheEvict(value = CACHE_NAME, key = "#result.id() + '_hierarchy'"),
                    @CacheEvict(value = CACHE_NAME, key = "'pageants'")
            }
    )
    public PageantSummaryDTO updatePageant(UUID id, PageantUpdateDTO pageantUpdateDTO) {
        Pageant pageant = pageantRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Can't update! Pageant not found.", ErrorCode.ENTITY_NOT_FOUND);
        });

        /* Manually do the status check. Don't use @RequirePageantStatus() */
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
        return mapper.toSummaryDTO(pageantRepository.save(pageant));
    }

    @Caching(
            put = @CachePut(value = CACHE_NAME, key = "#result.id()"),
            evict = {
                    @CacheEvict(value = CACHE_NAME, key = "#result.id() + '_hierarchy'"),
                    @CacheEvict(value = CACHE_NAME, key = "'pageants'")
            }
    )
    @Transactional
    public PageantSummaryDTO softResetPageant(UUID id) {
        Pageant pageant = pageantRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Can't reset! Pageant not found.", ErrorCode.ENTITY_NOT_FOUND);
        });

        /* Leaderboard reset */
        List<Award> awards =  awardRepository.findAllByPageant_Id(id);
        List<AwardLeaderboard> leaderboards = awards
                .stream()
                .flatMap(award -> award.getLeaderboard().stream())
                .toList();

        leaderboards.forEach(leaderboard -> leaderboard.setScore(0.0));
        awardLeaderboardRepository.saveAll(leaderboards);


        /* Scores reset */
        List<Score> scores = scoreRepository.findAll(
                Specification.allOf(ScoreSpecification.hasPageant(id))
        );
        scores.forEach(score -> score.setValue(0));
        scoreRepository.saveAll(scores);

        /* Reset Phase Statuses */
        List<Phase> phases = phaseRepository.findAllByPageant_Id(id);
        List<Segment> segments = phases
                .stream()
                .flatMap(phase -> phase.getSegments().stream())
                .toList();

        segments.forEach(segment -> segment.setStatus(PhaseSegmentStatus.PENDING));
        phases.forEach(phase -> phase.setStatus(PhaseSegmentStatus.PENDING));
        pageant.setStatus(PageantStatus.PREPARATION);

        /* Reset Candidate-Segment Qualifications */
        List<CandidateSegmentQualification> candidateSegmentQualifications = csqRepository.findAll(
                Specification.allOf(CandidateSegmentQualificationSpecification.hasPageant(id))
        );
        candidateSegmentQualifications.forEach(csq -> csq.setQualified(true));

        segmentRepository.saveAll(segments);
        phaseRepository.saveAll(phases);
        Pageant savedPageant = pageantRepository.save(pageant);

        return mapper.toSummaryDTO(savedPageant);
    }

    @Caching(
            evict = {
                    @CacheEvict(value = CACHE_NAME, key = "#id"),
                    @CacheEvict(value = CACHE_NAME, key = "#id + '_hierarchy'"),
                    @CacheEvict(value = CACHE_NAME, key = "'pageants'")
            }
    )
    public void deletePageant(UUID id) {
        Pageant pageant = pageantRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Can't delete! Pageant not found.", ErrorCode.ENTITY_NOT_FOUND);
        });

        /* Manually do the status check. Don't use @RequirePageantStatus() */
        if (
            pageant.getStatus() != PageantStatus.PREPARATION &&
            pageant.getStatus() != PageantStatus.CLOSED
        ) {
            throw new PageantAccessDeniedException(
                    "Can't delete! A pageant can only be deleted before or after starting.",
                    ErrorCode.PAGEANT_ACCESS_DENIED
            );
        }

        pageantRepository.deleteById(id);
    }
}
