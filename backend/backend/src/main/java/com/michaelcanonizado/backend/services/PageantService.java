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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PageantService {
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

    public PageantSummaryDTO addPageant(PageantCreateDTO pageantCreateDTO) {
        Pageant pageant = pageantRepository.save(mapper.toEntity(pageantCreateDTO));
        return mapper.toSummaryDTO(pageant);
    }

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

    public PageantSummaryDTO getPageant(UUID id) {
        Pageant pageant = pageantRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Pageant not found!", ErrorCode.ENTITY_NOT_FOUND);
        });

        return mapper.toSummaryDTO(pageant);
    }
    public PageantHierarchyDTO getPageantHierarchy(UUID id) {
        Pageant pageant = pageantRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Pageant not found!", ErrorCode.ENTITY_NOT_FOUND);
        });

        return mapper.toHierarchyDTO(pageant);
    }

    public List<PageantSummaryDTO> getPageants() {
        List<Pageant> pageants = pageantRepository.findAll();
        return pageants
                .stream()
                .map(pageant -> {
                    return mapper.toSummaryDTO(pageant);
                }).toList();
    }

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
