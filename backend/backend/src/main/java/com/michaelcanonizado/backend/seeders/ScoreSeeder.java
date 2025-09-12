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
    private final ScoreRepository scoreRepository;
    private final CandidateRepository candidateRepository;
    private final CriterionRepository criterionRepository;
    private final JudgeRepository judgeRepository;

    @Autowired
    public ScoreSeeder(ScoreRepository scoreRepository, CandidateRepository candidateRepository, CriterionRepository criterionRepository, JudgeRepository judgeRepository) {
        this.scoreRepository = scoreRepository;
        this.candidateRepository = candidateRepository;
        this.criterionRepository = criterionRepository;
        this.judgeRepository = judgeRepository;
    }

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
                    Score score = new Score(
                            faker.number().numberBetween(
                                    0,
                                    criterion.getMaxScore()
                            ),
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
