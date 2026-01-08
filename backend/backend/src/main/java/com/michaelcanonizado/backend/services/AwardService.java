package com.michaelcanonizado.backend.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.michaelcanonizado.backend.annotations.RequirePageantStatus;
import com.michaelcanonizado.backend.dtos.award.AwardDetailedDTO;
import com.michaelcanonizado.backend.dtos.award.AwardCreateDTO;
import com.michaelcanonizado.backend.dtos.award.AwardSummaryDTO;
import com.michaelcanonizado.backend.dtos.award.AwardUpdateDTO;
import com.michaelcanonizado.backend.dtos.criterion.CriterionBreakdownDTO;
import com.michaelcanonizado.backend.dtos.judge.JudgeBreakdownDTO;
import com.michaelcanonizado.backend.dtos.phase.PhaseBreakdownDTO;
import com.michaelcanonizado.backend.dtos.score.ScoreBreakdownDTO;
import com.michaelcanonizado.backend.dtos.segment.SegmentBreakdownDTO;
import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.customs.EntityNotFoundException;
import com.michaelcanonizado.backend.formula.FormulaTree;
import com.michaelcanonizado.backend.formula.FormulaTreeBuilder;
import com.michaelcanonizado.backend.formula.contexts.EvaluationContext;
import com.michaelcanonizado.backend.formula.contexts.FormulaContextFactory;
import com.michaelcanonizado.backend.formula.contexts.TypeContext;
import com.michaelcanonizado.backend.formula.values.NumberValue;
import com.michaelcanonizado.backend.mappers.*;
import com.michaelcanonizado.backend.models.*;
import com.michaelcanonizado.backend.repositories.*;
import com.michaelcanonizado.backend.contexts.PageantContext;
import com.michaelcanonizado.backend.specifications.ScoreSpecification;
import com.michaelcanonizado.backend.utilities.FormulaEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.MathContext;
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
        UUID selectedPageantId = pageantContext.getId();

        /* Convert serialized blockly workspace to AST */
        JsonNode serializedBlocklyWorkspace = award.getFormula().getWorkspace();
        FormulaTree formulaTree = formulaTreeBuilder.build(serializedBlocklyWorkspace);

        /* This will fail if the formula tree is invalid (Formula should already be validated on creation) */
        TypeContext typeContext = formulaContextFactory.createTypeContext();
        formulaTree.getFormulaNode().getType(typeContext);

        List<UUID> criterionIdsInFormula = new ArrayList<>(formulaTree.getCriterionIdsInFormula());
        MathContext mathContext = new MathContext(10);

        /* Fetch formula criteria */
        List<Criterion> criteria = criterionRepository.findAllById(criterionIdsInFormula);

        /* Fetch all candidates in the pageant */
        List<Candidate> candidates = candidateRepository.findAllByPageant_Id(selectedPageantId);
        /* Fetch relevant scores */
        List<Score> scores = scoreRepository.findAll(
                Specification.allOf(
                        ScoreSpecification.hasPageant(selectedPageantId),
                        ScoreSpecification.hasCandidates(
                                candidates.stream().map(Candidate::getId).toList()
                        ),
                        ScoreSpecification.hasCriteria(criterionIdsInFormula)
                )
        );

        /* Fetch candidates' award leaderboard rows and map them to their respective candidate */
        List<AwardLeaderboard> leaderboard = awardLeaderboardRepository.findAllByAward_Id(award.getId());
        /* Map<CandidateId, AwardLeaderboard> */
        Map<UUID, AwardLeaderboard> awardLeaderboardMap = leaderboard
                .stream()
                .collect(
                        Collectors.toMap(
                                leaderboardRow -> leaderboardRow.getCandidate().getId(),
                                Function.identity()
                        )
                );

        /* Create a template of CriterionBreakdowns per criterion as accessing
        the sub entities are expensive (fetched lazy). These entities can be
        cached instead in the future. */
        Map<UUID, CriteriaBreakdown> criterionBreakdownTemplates = new HashMap<>();
        criteria.forEach(criterion -> {
            PhaseBreakdownDTO phaseBreakdown = phaseMapper.toBreakdownDTO(criterion.getSegment().getPhase());
            SegmentBreakdownDTO segmentBreakdown = segmentMapper.toBreakdownDTO(criterion.getSegment());
            CriterionBreakdownDTO criterionBreakdown = criterionMapper.toBreakdownDTO(criterion);

            criterionBreakdownTemplates.put(
                    criterion.getId(),
                    new CriteriaBreakdown(
                            phaseBreakdown,
                            segmentBreakdown,
                            criterionBreakdown,
                            null,
                            null
                    )
            );
        });

        /* Group scores by criterion and candidate */
        /* Map<CandidateId, Map<CriterionId, List<Scores>>> */
        Map<UUID, Map<UUID, List<Score>>> scoreMap = new HashMap<>();
        for (Score score : scores) {
            UUID candidateId = score.getCandidate().getId();
            UUID criterionId = score.getCriterion().getId();

            scoreMap
                    .computeIfAbsent(candidateId, k -> new HashMap<>())
                    .computeIfAbsent(criterionId, k -> new ArrayList<>())
                    .add(score);
        }

        /* Map of criteriaAverages per candidate. Each sub map will be used in the evaluation
        * context, when evaluating the formula for each candidate. */
        /* Map<CandidateId, Map<CriterionId, Average Score>> */
        Map<UUID, Map<UUID, BigDecimal>> candidateCriteriaAverages = new HashMap<>();
        /* Map of criteriaBreakdowns for each candidate */
        /* Map<CandidateID, List<CriterionBreakdown>> */
        Map<UUID, List<CriteriaBreakdown>> candidateCriteriaBreakdowns =  new HashMap<>();

        /* Populate candidateCriterionAverages and candidateCriteriaBreakdowns.
        * This is done together to keep the result of the evaluated formula in-sync
        * with the criterion breakdown. Loop through each candidate and get the needed data */
        candidates.forEach(candidate -> {
            UUID candidateId = candidate.getId();

            /* Collect the candidate's breakdowns  */
            List<CriteriaBreakdown> breakdowns = new ArrayList<>();

            /* Loop through each criterion in the formula */
            criteria.forEach(criterion -> {
                UUID criterionId = criterion.getId();

                /* Get the scores of the candidate on the current criterion */
                List<Score> candidateScores = scoreMap.get(candidateId).get(criterionId);

                List<ScoreBreakdownDTO> scoreBreakdowns = new ArrayList<>();
                BigDecimal sum = BigDecimal.ZERO;

                /* Go through each score */
                for (Score score : candidateScores) {
                    /* Create the score breakdown */
                    Judge judge = score.getJudge();
                    scoreBreakdowns.add(
                            new ScoreBreakdownDTO(
                                    new JudgeBreakdownDTO(
                                            judge.getId(),
                                            judge.getUsername(),
                                            judge.getFirstName(),
                                            judge.getLastName(),
                                            judge.getHonorific(),
                                            judge.getNumber()
                                    ),
                                    score.getValue()
                            )
                    );

                    /* Compute sum of score values */
                    sum = sum.add(new BigDecimal(score.getValue()));
                }

                /* Get the average score of candidate on the current criterion */
                BigDecimal average = sum.divide(
                        BigDecimal.valueOf(candidateScores.size()),
                        mathContext
                );


                /* Create and add the CriterionBreakdown */
                CriteriaBreakdown template = criterionBreakdownTemplates.get(criterionId);
                breakdowns.add(
                        new CriteriaBreakdown(
                                template.getPhase(),
                                template.getSegment(),
                                template.getCriterion(),
                                /* CHANGE DATA TYPE TO BigDecimal */
                                average.doubleValue(),
                                scoreBreakdowns
                        )
                );

                /* Add to candidateCriteriaAverages */
                candidateCriteriaAverages
                        .computeIfAbsent(candidateId, k -> new HashMap<>())
                        .put(criterionId, average);
            });

            /* Add to candidateCriteriaBreakdowns */
            candidateCriteriaBreakdowns.put(candidateId, breakdowns);
        });

        /* Go through each candidate and evaluate the formula on them */
        candidates.forEach(candidate -> {
            /* Get their criterion scores */
            Map<UUID, BigDecimal> criteriaValues = candidateCriteriaAverages.get(candidate.getId());

            /* Populate their evaluation context */
            EvaluationContext evaluationContext = formulaContextFactory.createEvaluationContext(
                    mathContext,
                    criteriaValues
            );

            /* Evaluate the formula */
            BigDecimal result = ((NumberValue) formulaTree.getFormulaNode().evaluate(evaluationContext)).value();

            /* Set candidate's award leaderboard score value */
            AwardLeaderboard candidateLeaderboardRow = awardLeaderboardMap.get(candidate.getId());
            /* CHANGE AwardLeaderboard.score DATA TYPE TO BE BigDecimal! */
            candidateLeaderboardRow.setScore(result.doubleValue());

            /* Add candidate's breakdown */
            candidateLeaderboardRow.setCriteriaBreakdown(
                    candidateCriteriaBreakdowns.get(candidate.getId())
            );
        });

        awardLeaderboardRepository.saveAll(leaderboard);
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
