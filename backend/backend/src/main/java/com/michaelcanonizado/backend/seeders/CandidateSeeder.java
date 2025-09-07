package com.michaelcanonizado.backend.seeders;

import com.github.javafaker.Faker;
import com.michaelcanonizado.backend.models.Candidate;
import com.michaelcanonizado.backend.models.CandidateGender;
import com.michaelcanonizado.backend.models.College;
import com.michaelcanonizado.backend.repositories.CandidateRepository;
import com.michaelcanonizado.backend.repositories.CollegeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Order(5)
public class CandidateSeeder implements CommandLineRunner {
    private final Faker faker = new Faker();
    private final CandidateRepository candidateRepository;
    private final CollegeRepository collegeRepository;

    @Autowired
    public CandidateSeeder(CandidateRepository candidateRepository, CollegeRepository collegeRepository) {
        this.candidateRepository = candidateRepository;
        this.collegeRepository = collegeRepository;
    }

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        List<College> colleges =  collegeRepository.findAll();
        for (int i = 1; i <= colleges.size(); i++) {
            College college = colleges.get(i-1);
            candidateRepository.save(new Candidate(i, faker.name().firstName(), faker.name().lastName(), CandidateGender.FEMALE, 20, college));
            candidateRepository.save(new Candidate(i, faker.name().firstName(), faker.name().lastName(), CandidateGender.MALE, 20, college));
        }
    }
}
