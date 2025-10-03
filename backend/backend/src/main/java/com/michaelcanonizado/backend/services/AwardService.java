package com.michaelcanonizado.backend.services;

import com.michaelcanonizado.backend.annotations.RequirePageantStatus;
import com.michaelcanonizado.backend.dtos.AwardLeaderboardSummaryDTO;
import com.michaelcanonizado.backend.dtos.award.AwardCreateDTO;
import com.michaelcanonizado.backend.dtos.award.AwardSummaryDTO;
import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.customs.EntityNotFoundException;
import com.michaelcanonizado.backend.mappers.AwardLeaderboardMapper;
import com.michaelcanonizado.backend.mappers.AwardMapper;
import com.michaelcanonizado.backend.models.*;
import com.michaelcanonizado.backend.repositories.*;
import com.michaelcanonizado.backend.contexts.PageantContext;
import com.michaelcanonizado.backend.specifications.ScoreSpecification;
import com.michaelcanonizado.backend.utilities.AwardFormulaEncoder;
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
    private AwardLeaderboardRepository awardLeaderboardRepository;

    @Autowired
    private PageantRepository pageantRepository;

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private AwardMapper awardMapper;

    @Autowired
    private AwardLeaderboardMapper awardLeaderboardMapper;

    @Autowired
    private PageantContext pageantContext;

    @Autowired
    private AwardFormulaEncoder formulaEncoder;

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

        /* Encode formula to SpEL safe format */
        String rawFormula = award.getFormula();
        String encodedFormula = formulaEncoder.encodeFormula(rawFormula);
        award.setFormula(encodedFormula);

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

        /* Decode formula back to raw form since
           created award will be returned back. */
        encodedFormula = savedAward.getFormula();
        String decodedFormula = formulaEncoder.decodeFormula(encodedFormula);
        savedAward.setFormula(decodedFormula);
        return awardMapper.toSummaryDTO(savedAward);
    }

    @RequirePageantStatus({
            PageantStatus.PREPARATION,
            PageantStatus.ONGOING
    })
    public List<AwardSummaryDTO> getAwards() {
        UUID selectedPageantId = pageantContext.getId();

        List<Award> awards = awardRepository.findAllByPageant_Id(selectedPageantId);

        return awards
                .stream()
                .map(award -> {
                    String encodedFormula = award.getFormula();
                    String decodedFormula = formulaEncoder.decodeFormula(encodedFormula);
                    award.setFormula(decodedFormula);

                    return awardMapper.toSummaryDTO(award);
                })
                .toList();
    }

    @Transactional
    public List<AwardLeaderboardSummaryDTO> getAwardResult(UUID id) {
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

        List<UUID> candidateIds = candidateRepository
                                    .findAllByPageant_Id(selectedPageantId)
                                    .stream()
                                    .map(Candidate::getId)
                                    .toList();

        /* Fetch relevant scores: */
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

        /* Get award's leaderboard (Pre-generate when creating award and candidate)
           At this point all rows should have score = 0. So just update them. */
        List<AwardLeaderboard> leaderboard = awardLeaderboardRepository.findAllByAward_Id(id);
        /* Map leaderboard by candidate id. This makes it more efficient and
           easier to perform update. O(1) lookup!
           ( Nested loops = NOOBS , lists = AMATEUR , hashmaps = PRO | XD ). */
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
        /* Loop through all candidates and use their criterion averages to fill the formula */
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
            AwardLeaderboard row = candidateRowsInLeaderboard.get(candidateId);
            row.setScore(result);

            String substitutedFormula = award.getFormula();
            for (Map.Entry<String, Double> entry : criterionAverages.entrySet()) {
                // entry.getKey() is something like "C1234abcd..."
                // but note: in the formula it's "#C1234abcd..."
                substitutedFormula = substitutedFormula.replace(
                        "#" + entry.getKey(),
                        entry.getValue().toString()
                );
            }
            System.out.println(candidateId + " : " + substitutedFormula);
        });
        /* Save updated leaderboard */
        List<AwardLeaderboard> updatedLeaderboard = awardLeaderboardRepository.saveAll(candidateRowsInLeaderboard.values());

        return updatedLeaderboard
                .stream()
                .sorted(Comparator.comparing(AwardLeaderboard::getScore).reversed())
                .limit(award.getCandidateLimit())
                .map(candidateRow -> {
                    return awardLeaderboardMapper.toSummaryDTO(candidateRow);
                })
                .toList();
    }
}
