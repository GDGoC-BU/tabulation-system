package com.gdgocbu.tabulation.backend.seeders;

import com.github.javafaker.Faker;
import com.gdgocbu.tabulation.backend.models.Candidate;
import com.gdgocbu.tabulation.backend.models.CandidateGender;
import com.gdgocbu.tabulation.backend.models.College;
import com.gdgocbu.tabulation.backend.models.Pageant;
import com.gdgocbu.tabulation.backend.repositories.CandidateRepository;
import com.gdgocbu.tabulation.backend.repositories.CollegeRepository;
import com.gdgocbu.tabulation.backend.repositories.PageantRepository;
import com.gdgocbu.tabulation.backend.utilities.CollegeCandidateData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
public class CandidateSeeder implements DatabaseSeeder {
    private final Faker faker = new Faker();

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private CollegeRepository collegeRepository;

    @Autowired
    private PageantRepository pageantRepository;


    private void seedRandom() {
        Pageant pageant = pageantRepository.findAll().getFirst();
        List<College> colleges =  collegeRepository.findAll();

        List<CandidateGender> genders = Arrays.asList(CandidateGender.FEMALE, CandidateGender.MALE, CandidateGender.OTHER);
        Random random = new Random();
        double diverseGenderProbability = 0;

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

    private void seedRealData() {
        Pageant pageant = pageantRepository.findAll().getFirst();
        List<College> colleges =  collegeRepository.findAll();
        List<Candidate> candidates = new ArrayList<>();

        for (College college : colleges) {
            CollegeCandidateData.CollegeTemp collegeTemp = CollegeCandidateData.collegesByCode.get(college.getCode());

            if (collegeTemp != null) {
                for (CollegeCandidateData.CandidateTemp candidateTemp : collegeTemp.getCandidates()) {
                    Candidate candidate = new Candidate(
                            collegeTemp.getNumber(),
                            candidateTemp.getFirstName(),
                            candidateTemp.getLastName(),
                            candidateTemp.getGender(),
                            candidateTemp.getAge(),
                            college,
                            pageant
                            );
                    candidates.add(candidate);
                }
            } else {
                System.err.println("No seed data for " + college.getName());
            }
        }

        candidateRepository.saveAll(candidates);
    }

    @Transactional
    @Override
    public void seed() {
        if (false) seedRandom();
        if (true) seedRealData();
    }
}
