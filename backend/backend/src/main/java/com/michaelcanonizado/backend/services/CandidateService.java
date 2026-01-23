package com.michaelcanonizado.backend.services;

import com.michaelcanonizado.backend.annotations.RequirePageantStatus;
import com.michaelcanonizado.backend.dtos.candidate.CandidateCreateDTO;
import com.michaelcanonizado.backend.dtos.candidate.CandidateSummaryDTO;
import com.michaelcanonizado.backend.dtos.candidate.CandidateUpdateDTO;
import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.customs.EntityNotFoundException;
import com.michaelcanonizado.backend.exceptions.customs.PageantAccessDeniedException;
import com.michaelcanonizado.backend.mappers.CandidateMapper;
import com.michaelcanonizado.backend.models.*;
import com.michaelcanonizado.backend.repositories.*;
import com.michaelcanonizado.backend.contexts.PageantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class CandidateService {
    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private SegmentRepository segmentRepository;

    @Autowired
    private JudgeRepository judgeRepository;

    @Autowired
    private CriterionRepository criterionRepository;

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private PageantRepository pageantRepository;

    @Autowired
    private AwardRepository awardRepository;

    @Autowired
    private AwardLeaderboardRepository awardLeaderboardRepository;

    @Autowired
    private CandidateMapper mapper;

    @Autowired
    private PageantContext pageantContext;

    @RequirePageantStatus({
            PageantStatus.PREPARATION
    })
    @Transactional
    public CandidateSummaryDTO addCandidate(CandidateCreateDTO candidateCreateDTO) {
        /* Load DTO to Entity */
        Candidate candidate = mapper.toEntity(candidateCreateDTO);

        /* Connect to the selected pageant */
        UUID selectedPageantId = pageantContext.getId();
        Pageant pageant = pageantRepository.findById(selectedPageantId).orElseThrow(() -> {
            return new PageantAccessDeniedException(
                    "Pageant not found! Can't perform operation",
                    ErrorCode.PAGEANT_ACCESS_DENIED
            );
        });
        candidate.setPageant(pageant);

        Candidate savedCandidate = candidateRepository.save(candidate);

        /* Pre-generate the scores for the new candidate */
        List<Judge> judges = judgeRepository.findAll();
        List<Criterion> criteria = criterionRepository.findAll();
        List<Score> newScores = new ArrayList<>();
        judges.forEach(judge -> {
            criteria.forEach(criterion -> {
                newScores.add(new Score(0, judge, savedCandidate, criterion));
            });
        });
        /* Batch save to minimize insert queries */
        scoreRepository.saveAll(newScores);

        /* Get available awards and pre-generate
           candidate rows in the award's leaderboard */
        List<Award> awards = awardRepository.findAllByPageant_Id(selectedPageantId);
        List<AwardLeaderboard> awardLeaderboards = new ArrayList<>();
        awards.forEach(award -> {
            awardLeaderboards.add(
                    new AwardLeaderboard(
                            BigDecimal.ZERO,
                            savedCandidate,
                            award
                    )
            );
        });
        /* Batch save to minimize insert queries */
        awardLeaderboardRepository.saveAll(awardLeaderboards);

        /* Save candidate to database */
        return mapper.toSummaryDTO(savedCandidate);
    }

    @Transactional
    @RequirePageantStatus({
            PageantStatus.PREPARATION,
            PageantStatus.ONGOING,
            PageantStatus.FINALIZING,
            PageantStatus.CLOSED
    })
    public CandidateSummaryDTO getCandidate(UUID id) {
        Candidate candidate = candidateRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Candidate not found!", ErrorCode.ENTITY_NOT_FOUND);
        });

        pageantContext.assertAccess(candidate.getPageant().getId());

        return mapper.toSummaryDTO(candidate);
    }

    @Transactional
    @RequirePageantStatus({
            PageantStatus.PREPARATION,
            PageantStatus.ONGOING,
            PageantStatus.FINALIZING,
            PageantStatus.CLOSED
    })
    public List<CandidateSummaryDTO> getCandidates() {
        UUID selectedPageantId = pageantContext.getId();
        List<Candidate> candidates = candidateRepository.findAllByPageant_Id(selectedPageantId);

        return candidates
                .stream()
                .sorted(Comparator.comparing(Candidate::getNumber))
                .map(mapper::toSummaryDTO)
                .toList();
    }

    @RequirePageantStatus({
            PageantStatus.PREPARATION,
    })
    public CandidateSummaryDTO updateCandidate(UUID id, CandidateUpdateDTO candidateUpdateDTO) {
        Candidate candidate = candidateRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Can't update! Candidate not found.", ErrorCode.ENTITY_NOT_FOUND);
        });

        pageantContext.assertAccess(candidate.getPageant().getId());

        mapper.updateEntityFromDTO(candidate, candidateUpdateDTO);
        return mapper.toSummaryDTO(candidateRepository.save(candidate));
    }

    @RequirePageantStatus({
            PageantStatus.PREPARATION,
    })
    @Transactional
    public void deleteCandidate(UUID id) {
        Candidate candidate = candidateRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Can't delete! Candidate not found.", ErrorCode.ENTITY_NOT_FOUND);
        });
        pageantContext.assertAccess(
                candidate.getPageant()
                        .getId()
        );
        candidateRepository.delete(candidate);
    }
}
