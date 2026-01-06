package com.michaelcanonizado.backend.services;

import com.michaelcanonizado.backend.annotations.RequirePageantStatus;
import com.michaelcanonizado.backend.dtos.award.AwardDetailedDTO;
import com.michaelcanonizado.backend.dtos.award.AwardCreateDTO;
import com.michaelcanonizado.backend.dtos.award.AwardSummaryDTO;
import com.michaelcanonizado.backend.dtos.award.AwardUpdateDTO;
import com.michaelcanonizado.backend.dtos.criterion.CriterionBreakdownDTO;
import com.michaelcanonizado.backend.dtos.phase.PhaseBreakdownDTO;
import com.michaelcanonizado.backend.dtos.score.ScoreBreakdownDTO;
import com.michaelcanonizado.backend.dtos.segment.SegmentBreakdownDTO;
import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.customs.EntityNotFoundException;
import com.michaelcanonizado.backend.mappers.*;
import com.michaelcanonizado.backend.models.*;
import com.michaelcanonizado.backend.repositories.*;
import com.michaelcanonizado.backend.contexts.PageantContext;
import com.michaelcanonizado.backend.specifications.ScoreSpecification;
import com.michaelcanonizado.backend.utilities.FormulaEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AwardService {
    @Autowired
    private AwardRepository awardRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private CriterionRepository criterionRepository;

    @Autowired
    private AwardLeaderboardRepository awardLeaderboardRepository;

    @Autowired
    private PageantRepository pageantRepository;

    @Autowired
    private ScoreRepository scoreRepository;

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
    private AwardLeaderboardMapper awardLeaderboardMapper;

    @Autowired
    private PageantContext pageantContext;

    @Autowired
    private FormulaEncoder formulaEncoder;

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

        /* Get available candidates and pre-generate
           their rows in the award's leaderboard */
        List<Candidate> candidates = candidateRepository.findAllByPageant_Id(selectedPageantId);
        List<AwardLeaderboard> awardLeaderboards = new ArrayList<>();
        candidates.forEach(candidate -> {
            awardLeaderboards.add(
                    new AwardLeaderboard(
                            0.0,
                            candidate,
                            savedAward
                    )
            );
        });
        /* Batch save to minimize insert queries */
        awardLeaderboardRepository.saveAll(awardLeaderboards);

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
    public AwardDetailedDTO calculateAwardResult(UUID id) {
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
