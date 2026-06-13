package com.gdgocbu.tabulation.backend.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.gdgocbu.tabulation.backend.contexts.PageantContext;
import com.gdgocbu.tabulation.backend.dtos.criterion.CriterionBreakdownDTO;
import com.gdgocbu.tabulation.backend.dtos.judge.JudgeBreakdownDTO;
import com.gdgocbu.tabulation.backend.dtos.phase.PhaseBreakdownDTO;
import com.gdgocbu.tabulation.backend.dtos.score.ScoreBreakdownDTO;
import com.gdgocbu.tabulation.backend.dtos.segment.SegmentBreakdownDTO;
import com.gdgocbu.tabulation.backend.formula.FormulaTree;
import com.gdgocbu.tabulation.backend.formula.FormulaTreeBuilder;
import com.gdgocbu.tabulation.backend.formula.contexts.EvaluationContext;
import com.gdgocbu.tabulation.backend.formula.contexts.FormulaContextFactory;
import com.gdgocbu.tabulation.backend.formula.contexts.TypeContext;
import com.gdgocbu.tabulation.backend.formula.values.NumberValue;
import com.gdgocbu.tabulation.backend.mappers.CriterionMapper;
import com.gdgocbu.tabulation.backend.mappers.LeaderboardMapper;
import com.gdgocbu.tabulation.backend.mappers.PhaseMapper;
import com.gdgocbu.tabulation.backend.mappers.SegmentMapper;
import com.gdgocbu.tabulation.backend.models.*;
import com.gdgocbu.tabulation.backend.repositories.*;
import com.gdgocbu.tabulation.backend.specifications.ScoreSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class LeaderboardService {
    @Autowired
    private LeaderboardRepository leaderboardRepository;

    @Autowired
    private LeaderboardEntryRepository leaderboardEntryRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private CriterionRepository criterionRepository;

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private LeaderboardMapper leaderboardMapper;

    @Autowired
    private PhaseMapper phaseMapper;

    @Autowired
    private SegmentMapper segmentMapper;

    @Autowired
    private CriterionMapper criterionMapper;

    @Autowired
    private PageantContext pageantContext;

    @Autowired
    private FormulaTreeBuilder formulaTreeBuilder;

    @Autowired
    private FormulaContextFactory formulaContextFactory;

    public Formula validateLeaderboardFormula(Formula formula) {
        /* Construct the tree. Will throw error if there are mismatch blocks. */
        JsonNode serializedBlocklyWorkspace = formula.getWorkspace();
        FormulaTree formulaTree = formulaTreeBuilder.build(serializedBlocklyWorkspace);

        /* Validate the tree node types. Will throw error if block input and output types don't match. */
        TypeContext typeContext = formulaContextFactory.createTypeContext();
        formulaTree.getRootNode().getType(typeContext);

        /* Re-visit how this can be properly handled and reused by other methods. This method also mutates the
        * already pass by reference formula and the called no longer needs to re set the formula field. */
        formula.setTree(formulaTree.getRootNode());
        return formula;
    }

    @Transactional
    public Leaderboard calculateLeaderboard(
            Leaderboard leaderboard,
            List<Candidate> candidates
    ) {
        UUID selectedPageantId = pageantContext.getId();

        /* ======================================================================== */
        /* ========================= Formula Tree Building ======================== */
        /* ======================================================================== */
        /* Convert serialized blockly workspace to formula tree */
        JsonNode serializedBlocklyWorkspace = leaderboard.getFormula().getWorkspace();
        FormulaTree formulaTree = formulaTreeBuilder.build(serializedBlocklyWorkspace);
        /* This will throw an exception if the formula tree generated is invalid
         * (Formula should already be validated on creation) */
        TypeContext typeContext = formulaContextFactory.createTypeContext();
        formulaTree.getRootNode().getType(typeContext);
        /* MathContext the formula will use. Set the formula precision here */
        MathContext mathContext = new MathContext(10, RoundingMode.HALF_UP);

        /* ======================================================================== */
        /* ======================= Data Fetching & Aggregation ==================== */
        /* ======================================================================== */

        List<UUID> criterionIdsInFormula = new ArrayList<>(formulaTree.getCriterionIdsInFormula());

        /* Fetch criteria in the formula */
        List<Criterion> criteria = criterionRepository.findAllById(criterionIdsInFormula);

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

        /* ======================================================================== */
        /* =========================== Context Building =========================== */
        /* ======================================================================== */

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
                BigDecimal average = candidateScores.isEmpty()
                        ? BigDecimal.ZERO
                        : sum.divide(
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
                                average,
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

        /* ======================================================================== */
        /* ============================== EVALUATION ============================== */
        /* ======================================================================== */

        leaderboard.getEntries().clear();
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
            BigDecimal result = ((NumberValue) formulaTree.getRootNode().evaluate(evaluationContext)).value();

            /* Create candidate's entry */
            LeaderboardEntry leaderboardEntry = new LeaderboardEntry(candidate);
            leaderboardEntry.setScore(result);

            /* Add candidate's breakdown */
            leaderboardEntry.setCriteriaBreakdown(
                    candidateCriteriaBreakdowns.get(candidate.getId())
            );

            leaderboard.addEntry(leaderboardEntry);
        });

        rankAndSelectLeaderboardEntries(leaderboard.getEntries(), leaderboard.getSelectionCount());
        leaderboard.setLastCalculatedAt(LocalDateTime.now());
        return leaderboardRepository.save(leaderboard);
    }

    private void rankAndSelectLeaderboardEntries(List<LeaderboardEntry> entries, int selectionCount) {
        /* Separate the entries by gender and sort them in descending order.
        * NOTE: the temporary lists below are immutable to add, reorder, sorting, etc.
        * You can only modify the actual objects from then on. */
        List<LeaderboardEntry> femaleCandidates = entries
                .stream()
                .filter(entry -> entry.getCandidate().getGender() == CandidateGender.FEMALE)
                .sorted(Comparator.comparing(LeaderboardEntry::getScore).reversed())
                .toList();
        List<LeaderboardEntry> maleCandidates = entries
                .stream()
                .filter(entry -> entry.getCandidate().getGender() == CandidateGender.MALE)
                .sorted(Comparator.comparing(LeaderboardEntry::getScore).reversed())
                .toList();

        /* Go through the candidates and set the rank and isSelected */
        for (int i = 0; i < femaleCandidates.size(); i++) {
            int rank = i + 1;
            LeaderboardEntry entry = femaleCandidates.get(i);
            entry.setRank(rank);
            entry.setSelected(rank <= selectionCount);
            System.out.println("F-C"+entry.getCandidate().getNumber() + " = " + entry.isSelected());
        }
        for (int i = 0; i < maleCandidates.size(); i++) {
            int rank = i + 1;
            LeaderboardEntry entry = maleCandidates.get(i);
            entry.setRank(rank);
            entry.setSelected(rank <= selectionCount);
            System.out.println("M-C"+entry.getCandidate().getNumber() + " = " + entry.isSelected());
        }

        entries.clear();
        entries.addAll(femaleCandidates);
        entries.addAll(maleCandidates);
    }
}
