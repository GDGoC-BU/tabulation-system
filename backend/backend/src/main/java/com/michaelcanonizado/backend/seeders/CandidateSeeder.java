package com.michaelcanonizado.backend.seeders;

import com.github.javafaker.Faker;
import com.michaelcanonizado.backend.models.Candidate;
import com.michaelcanonizado.backend.models.College;
import com.michaelcanonizado.backend.models.Gender;
import com.michaelcanonizado.backend.repositories.CandidateRepository;
import com.michaelcanonizado.backend.repositories.CollegeRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
class CandidateItem {
    private String username;
    private String passwordHash;
}

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

    @Override
    public void run(String... args) throws Exception {
        List<College> colleges =  collegeRepository.findAll();
        for (int i = 1; i <= colleges.size(); i++) {
            College college = colleges.get(i-1);
            candidateRepository.save(new Candidate(i, faker.name().firstName(), faker.name().lastName(), Gender.FEMALE, 20, college));
            candidateRepository.save(new Candidate(i, faker.name().firstName(), faker.name().lastName(), Gender.MALE, 20, college));
        }

    }
}
