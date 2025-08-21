package com.michaelcanonizado.backend.seeders;

import com.michaelcanonizado.backend.models.Candidate;
import com.michaelcanonizado.backend.models.Criterion;
import com.michaelcanonizado.backend.models.Judge;
import com.michaelcanonizado.backend.models.Score;
import com.michaelcanonizado.backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(7)
public class ScoreSeeder implements CommandLineRunner {
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

    @Override
    public void run(String... args) throws Exception {
        List<Candidate> candidates = candidateRepository.findAll();
        List<Criterion> criteria = criterionRepository.findAll();
        List<Judge> judges = judgeRepository.findAll();

        candidates.forEach(candidate -> {
            criteria.forEach(criterion -> {
                scoreRepository.save(new Score(criterion.getMaxScore(), judges.get(0), candidate, criterion));
                scoreRepository.save(new Score(criterion.getMaxScore(), judges.get(1), candidate, criterion));
                scoreRepository.save(new Score(criterion.getMaxScore(), judges.get(2), candidate, criterion));
                scoreRepository.save(new Score(criterion.getMaxScore(), judges.get(3), candidate, criterion));
            });
        });
    }
}
