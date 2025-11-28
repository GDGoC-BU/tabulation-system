package com.michaelcanonizado.backend.seeders;

import com.github.javafaker.Faker;
import com.michaelcanonizado.backend.models.Candidate;
import com.michaelcanonizado.backend.models.Criterion;
import com.michaelcanonizado.backend.models.Judge;
import com.michaelcanonizado.backend.models.Score;
import com.michaelcanonizado.backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class ScoreSeeder implements DatabaseSeeder {
    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private CriterionRepository criterionRepository;

    @Autowired
    private JudgeRepository judgeRepository;

    @Transactional
    @Override
    public void seed() {
        Faker faker = new Faker();

        List<Candidate> candidates = candidateRepository.findAll();
        List<Criterion> criteria = criterionRepository.findAll();
        List<Judge> judges = judgeRepository.findAll();

        candidates.forEach(candidate -> {
            criteria.forEach(criterion -> {
                judges.forEach(judge -> {
                    int scoreMaxValue = criterion.getMaxScore();
                    int randomScoreValue;
                    if (scoreMaxValue <= 5) {
                        randomScoreValue = faker.number().numberBetween(
                                (int) Math.floor(scoreMaxValue * 0.5),
                                scoreMaxValue
                        );
                    } else if (scoreMaxValue <= 10) {
                        randomScoreValue = faker.number().numberBetween(
                                (int) Math.floor(scoreMaxValue * 0.6),
                                scoreMaxValue
                        );
                    } else {
                        randomScoreValue = faker.number().numberBetween(
                                (int) Math.floor(scoreMaxValue * 0.75),
                                scoreMaxValue
                        );
                    }

                    Score score = new Score(
//                            0,
                            randomScoreValue,
                            judge,
                            candidate,
                            criterion
                    );
                    criterion.addScore(score);
                    scoreRepository.save(score);
                });
                criterionRepository.save(criterion);
            });
        });
    }
}
