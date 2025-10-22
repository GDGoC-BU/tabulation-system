package com.michaelcanonizado.backend.services;

import com.michaelcanonizado.backend.annotations.RequirePageantStatus;
import com.michaelcanonizado.backend.dtos.award.AwardDetailedDTO;
import com.michaelcanonizado.backend.dtos.awardLeaderboard.AwardLeaderboardSummaryDTO;
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
            PageantStatus.FINALIZING
    })
    @Transactional
    public AwardDetailedDTO calculateAwardResult(UUID id) {
        /* NOTE: Use Maps to search for entities inside the candidate-formula
           evaluator for faster [O(1)] lookup! */

        UUID selectedPageantId = pageantContext.getId();

        /* Get award */
        Award award = awardRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException(
                    "Award not found!",
                    ErrorCode.ENTITY_NOT_FOUND
            );
        });

        /* Check if award being accessed belongs to the selected pageant */
        pageantContext.assertAccess(award.getPageant().getId());

        /* Extract criterion ids from formula */
        Set<UUID> criteriaIdsInFormula = formulaEncoder.extractEncodedUUIDs(award.getFormula());

        /* Fetch candidates, but only get ids */
        List<UUID> candidateIds = candidateRepository
                                    .findAllByPageant_Id(selectedPageantId)
                                    .stream()
                                    .map(Candidate::getId)
                                    .toList();

        /* Fetch the criteria in the formula */
        List<Criterion> criteriaInFormula = criterionRepository.findAllById(criteriaIdsInFormula);
        /* Load criteria in a Map for faster lookup */
        Map<UUID, Criterion> criteriaMap = criteriaInFormula
                .stream()
                .collect(
                        Collectors.toMap(
                                Criterion::getId,
                                Function.identity(),
                                (existing, replacement) -> existing
                        )
                );

        /* Pregenerate the phase-segment-criterion details for the breakdowns.
           All criteria in the formula for all candidates are the same, this prevents
           redundant queries when we build the whole breakdown inside the candidate-formula
           evaluator loop. (Rephrase this comment) */
        Map<UUID, CriteriaBreakdown> criteriaBreakdownTemplates = new HashMap<>();
        for (UUID criterionId : criteriaIdsInFormula) {
            Criterion criterion = criteriaMap.get(criterionId);

            PhaseBreakdownDTO phaseBreakdown = phaseMapper.toBreakdownDTO(criterion.getSegment().getPhase());
            SegmentBreakdownDTO segmentBreakdown = segmentMapper.toBreakdownDTO(criterion.getSegment());
            CriterionBreakdownDTO criterionBreakdown = criterionMapper.toBreakdownDTO(criterion);

            criteriaBreakdownTemplates.put(
                    criterionId,
                    new CriteriaBreakdown(phaseBreakdown, segmentBreakdown, criterionBreakdown, null, null)
            );
        }

        /* Fetch relevant scores */
        List<Score> scores = scoreRepository.findAll(
                Specification.allOf(
                        /* Scores that belong to the selected pageant */
                        ScoreSpecification.hasPageant(selectedPageantId),
                        /* Scores that belong to the fetched candidates */
                        ScoreSpecification.hasCandidates(candidateIds),
                        /* Scores for criteria that is included in the formula */
                        ScoreSpecification.hasCriteria(new ArrayList<>(criteriaIdsInFormula))
                )
        );

        /* Map to search for scores for a candidate over a criterion
           Map<CandidateID, Map<CriterionId, List<Score>>> */
        Map<UUID, Map<UUID, List<Score>>> scoreMap = new HashMap<>();
        for (Score score : scores) {
            UUID candidateId = score.getCandidate().getId();
            UUID criterionId = score.getCriterion().getId();

            scoreMap
                    .computeIfAbsent(candidateId, k -> new HashMap<>())
                    .computeIfAbsent(criterionId, k -> new ArrayList<>())
                    .add(score);
        }

        /* Aggregate the scores in a more operable format.
           Candidates will have a list of their Criterion
           score averages, being the average of the score
           of all Judges:

           {
                "candidate1": {
                    "poise and elegance": 3.5,
                    "beauty of figure": 4.5,
                    "stage presence" : 3.75
                },
                "candidate2": {
                    "poise and elegance": 2.75,
                    "beauty of figure": 5.0,
                    "stage presence" : 4.6
                },
           }

           But use the ids as keys. Criterion ids will already
           be encoded to the SpEL safe format as they will be
           used in the context lookup when evaluating the formula:

           {
                "candidateUUID1": {
                    "encodedUUID1": 3.5,
                    "encodedUUID2": 4.5,
                    "encodedUUID3" : 3.75
                },
                "candidateUUID2": {
                    "encodedCriterionUUID1": 2.75,
                    "encodedCriterionUUID2": 5.0,
                    "encodedCriterionUUID3" : 4.6
                },
           } */
        Map<UUID, Map<String, Double>> candidateCriterionAverages = scores
                .stream()
                .collect(
                        Collectors.groupingBy(
                                score -> {
                                    return score.getCandidate().getId();
                                },
                                Collectors.groupingBy(score -> {
                                        return formulaEncoder.encodeUUID(score.getCriterion().getId());
                                    },
                                    Collectors.averagingInt(Score::getValue)
                                )
                        )
                );

        /* Get award's leaderboard rows (Pre-generate when creating award and candidate)
           At this point all rows should have score = 0 and breakdown = [null]. So just update them. */
        List<AwardLeaderboard> leaderboard = awardLeaderboardRepository.findAllByAward_Id(id);
        /* Map leaderboard row to its candidate */
        Map<UUID, AwardLeaderboard> candidateRowsInLeaderboard =
                leaderboard
                    .stream()
                    .collect(
                      Collectors.toMap(
                              leaderboardRow -> leaderboardRow.getCandidate().getId(),
                              Function.identity()
                      )
                    );

        /* Create shared parser */
        ExpressionParser parser = new SpelExpressionParser();
        /* Parse the formula and reuse it. Parsing is expensive! */
        Expression expression = parser.parseExpression(award.getFormula());
        /* Loop through all candidates evaluate the formula */
        candidateCriterionAverages.forEach((candidateId, criterionAverages) -> {
            /* Load the criterion averages into context */
            StandardEvaluationContext context = new StandardEvaluationContext(criterionAverages);

            /* Substitute the #criterionUUID using criterionAverages.

               E.g:

               Given averages:
               {
                    "#C1234": 85.5,
                    "#C5678": 90.0
               }

               Given formula: "0.4 * #C1234 + 0.4 * #C5678"
               Context.setVariable() result: "0.4 * 85.5 + 0.4 * 90.0" */
            criterionAverages.forEach(context::setVariable);

            /* Evaluate the expression: "0.4 * 85.5 + 0.4 * 90.0" --> 88.2
               This is then the candidate's score for the award */
            Double result = expression.getValue(context, Double.class);

            /* Get candidate's award leaderboard row */
            AwardLeaderboard leaderboardRow = candidateRowsInLeaderboard.get(candidateId);
            leaderboardRow.setScore(result);

            List<CriteriaBreakdown> criteriaBreakdowns = new ArrayList<>();
            /* Go through the criteria in the formula and build the breakdown */
            criteriaIdsInFormula.forEach(criterionId -> {
                /* Get scores for the current candidate and current criterion */
                List<Score> scoresForCriterion = scoreMap
                        .getOrDefault(candidateId, Map.of())
                        .getOrDefault(criterionId, List.of());

                /* Get pregenerated breakdown for the current criterion */
                CriteriaBreakdown breakdownTemplate = criteriaBreakdownTemplates.get(criterionId);

                /* Calculate the average score for the current criterion */
                Double averageScore = scoresForCriterion.stream()
                        .mapToInt(Score::getValue)
                        .average()
                        .orElse(0.0);

                List<ScoreBreakdownDTO> scoresBreakdown = scoresForCriterion.stream().map(score -> {
                    return scoreMapper.toBreakdownDTO(score);
                }).toList();

                CriteriaBreakdown criteriaBreakdown = new CriteriaBreakdown(
                        breakdownTemplate.getPhase(),
                        breakdownTemplate.getSegment(),
                        breakdownTemplate.getCriterion(),
                        averageScore,
                        scoresBreakdown
                );
                criteriaBreakdowns.add(criteriaBreakdown);
            });
            /* CriterionBreakdown is stored as JSONB. Completely change it so JPA notices the change */
            leaderboardRow.setCriteriaBreakdown(criteriaBreakdowns);
        });
        /* Save updated leaderboard */
        List<AwardLeaderboard> updatedLeaderboard = awardLeaderboardRepository.saveAll(candidateRowsInLeaderboard.values());
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
