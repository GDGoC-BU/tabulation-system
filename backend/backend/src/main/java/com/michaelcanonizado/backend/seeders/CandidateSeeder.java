package com.michaelcanonizado.backend.seeders;

import com.github.javafaker.Faker;
import com.michaelcanonizado.backend.models.Candidate;
import com.michaelcanonizado.backend.models.CandidateGender;
import com.michaelcanonizado.backend.models.College;
import com.michaelcanonizado.backend.models.Pageant;
import com.michaelcanonizado.backend.repositories.CandidateRepository;
import com.michaelcanonizado.backend.repositories.CollegeRepository;
import com.michaelcanonizado.backend.repositories.PageantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Component
public class CandidateSeeder implements DatabaseSeeder {
    private final Faker faker = new Faker();
    private final CandidateRepository candidateRepository;
    private final CollegeRepository collegeRepository;
    private final PageantRepository pageantRepository;

    @Autowired
    public CandidateSeeder(CandidateRepository candidateRepository, CollegeRepository collegeRepository, PageantRepository pageantRepository) {
        this.candidateRepository = candidateRepository;
        this.collegeRepository = collegeRepository;
        this.pageantRepository = pageantRepository;
    }

    @Transactional
    @Override
    public void seed() {
        Pageant pageant = pageantRepository.findAll().getFirst();
        List<College> colleges =  collegeRepository.findAll();

        List<CandidateGender> genders = Arrays.asList(CandidateGender.FEMALE, CandidateGender.MALE, CandidateGender.OTHER);
        Random random = new Random();
        double diverseGenderProbability = 0.4;

        for (int i = 1; i <= colleges.size(); i++) {
            College college = colleges.get(i-1);

            if (random.nextDouble() < diverseGenderProbability) {
                /* Chance to get an OTHER gender candidate, but they should be distinct.
                   Not candidates of a college should hae the same gender. */
                Collections.shuffle(genders);
                CandidateGender gender1 = genders.get(0);
                CandidateGender gender2 = genders.get(1);

                candidateRepository.save(new Candidate(i, faker.name().firstName(), faker.name().lastName(), gender1, 20, college, pageant));
                candidateRepository.save(new Candidate(i, faker.name().firstName(), faker.name().lastName(), gender2, 20, college, pageant));
            } else {
                /* Default FEMALE + MALE */
                candidateRepository.save(new Candidate(i, faker.name().firstName(), faker.name().lastName(), CandidateGender.FEMALE, 20, college, pageant));
                candidateRepository.save(new Candidate(i, faker.name().firstName(), faker.name().lastName(), CandidateGender.MALE, 20, college, pageant));
            }
        }
    }
}
