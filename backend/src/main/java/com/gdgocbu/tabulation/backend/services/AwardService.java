package com.gdgocbu.tabulation.backend.services;

import com.gdgocbu.tabulation.backend.annotations.RequirePageantStatus;
import com.gdgocbu.tabulation.backend.dtos.award.AwardDetailedDTO;
import com.gdgocbu.tabulation.backend.dtos.award.AwardCreateDTO;
import com.gdgocbu.tabulation.backend.dtos.award.AwardSummaryDTO;
import com.gdgocbu.tabulation.backend.dtos.award.AwardUpdateDTO;
import com.gdgocbu.tabulation.backend.exceptions.common.ErrorCode;
import com.gdgocbu.tabulation.backend.exceptions.customs.EntityNotFoundException;
import com.gdgocbu.tabulation.backend.formula.FormulaTreeBuilder;
import com.gdgocbu.tabulation.backend.formula.contexts.FormulaContextFactory;
import com.gdgocbu.tabulation.backend.mappers.*;
import com.gdgocbu.tabulation.backend.models.*;
import com.gdgocbu.tabulation.backend.repositories.*;
import com.gdgocbu.tabulation.backend.contexts.PageantContext;
import com.gdgocbu.tabulation.backend.utilities.FormulaEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class AwardService {
    @Autowired
    private AwardRepository awardRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private CriterionRepository criterionRepository;

    @Autowired
    private PageantRepository pageantRepository;

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private LeaderboardService leaderboardService;

    @Autowired
    private AwardMapper awardMapper;

    @Autowired
    private PhaseMapper phaseMapper;

    @Autowired
    private SegmentMapper segmentMapper;

    @Autowired
    private CriterionMapper criterionMapper;

    @Autowired
    private JudgeMapper judgeMapper;

    @Autowired
    private ScoreMapper scoreMapper;

    @Autowired
    private PageantContext pageantContext;

    @Autowired
    private FormulaEncoder formulaEncoder;

    @Autowired
    private FormulaTreeBuilder formulaTreeBuilder;

    @Autowired
    private FormulaContextFactory formulaContextFactory;

    @RequirePageantStatus({
            PageantStatus.PREPARATION,
    })
    public AwardSummaryDTO addAward(AwardCreateDTO awardCreateDTO) {
        /* Map DTO to entity */
        Award award = awardMapper.toEntity(awardCreateDTO);

        /* Connect to the selected pageant */
        UUID selectedPageantId = pageantContext.getId();
        Pageant pageant = pageantRepository.findById(selectedPageantId).orElseThrow(() -> {
            return new EntityNotFoundException(
                    "Cannot create candidate! Pageant being connected to it doesn't exist.",
                    ErrorCode.ENTITY_NOT_FOUND
            );
        });
        award.setPageant(pageant);

        /* Save Award */
        Award savedAward = awardRepository.save(award);

        // Leaderboard

        return awardMapper.toSummaryDTO(savedAward);
    }

    @RequirePageantStatus({
            PageantStatus.PREPARATION,
            PageantStatus.ONGOING,
            PageantStatus.FINALIZING,
            PageantStatus.CLOSED
    })
    public AwardDetailedDTO getAward(UUID id) {
        Award award = awardRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException(
                    "Award not found!",
                    ErrorCode.ENTITY_NOT_FOUND
            );
        });

        pageantContext.assertAccess(award.getPageant().getId());

        return awardMapper.toDetailedDTO(award);
    }

    @RequirePageantStatus({
            PageantStatus.PREPARATION,
            PageantStatus.ONGOING,
            PageantStatus.FINALIZING,
            PageantStatus.CLOSED
    })
    public List<AwardSummaryDTO> getAwards() {
        UUID selectedPageantId = pageantContext.getId();

        List<Award> awards = awardRepository.findAllByPageant_Id(selectedPageantId);
        return awards
                .stream()
                .map(award -> awardMapper.toSummaryDTO(award))
                .toList();
    }

    @RequirePageantStatus({
            PageantStatus.PREPARATION,
            PageantStatus.ONGOING,
            PageantStatus.FINALIZING,
            PageantStatus.CLOSED,
    })
    @Transactional
    public void calculateLeaderboard(UUID id) {
        Award award = awardRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException(
                    "Award not found!",
                    ErrorCode.ENTITY_NOT_FOUND
            );
        });
        pageantContext.assertAccess(award.getPageant().getId());
        UUID selectedPageantId = pageantContext.getId();

        Leaderboard leaderboard = award.getLeaderboard();
        if (leaderboard == null) {
            // Throw custom error
            throw new RuntimeException("");
        }

        List<Candidate> candidates = candidateRepository.findAllByPageant_Id(selectedPageantId);
        leaderboardService.calculateLeaderboard(
                leaderboard,
                candidates
        );
    }

    @RequirePageantStatus({
            PageantStatus.PREPARATION
    })
    public AwardSummaryDTO updateAward(UUID id, AwardUpdateDTO awardUpdateDTO) {
        Award award = awardRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException(
                    "Can't update! Award not found.",
                    ErrorCode.ENTITY_NOT_FOUND
            );
        });

        pageantContext.assertAccess(award.getPageant().getId());

        awardMapper.updateEntityFromDTO(award, awardUpdateDTO);
        return awardMapper.toSummaryDTO(awardRepository.save(award));
    }
}
