package com.michaelcanonizado.backend.seeders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SeederOrchestrator implements CommandLineRunner {
    private final List<DatabaseSeeder> seeders;

    @Autowired
    public SeederOrchestrator(
            PageantSeeder pageantSeeder,
            CandidateSeeder candidateSeeder,
            CandidateSegmentQualificationSeeder candidateSegmentQualificationSeeder,
            CollegeSeeder collegeSeeder,
            CriterionSeeder criterionSeeder,
            JudgeSeeder judgeSeeder,
            AdminSeeder adminSeeder,
            ScoreSeeder scoreSeeder,
            SegmentSeeder segmentSeeder,
            PhaseSeeder phaseSeeder
    ) {
        /* Order of the seeders in this list determine
           the order of execution of seeders */
        this.seeders = List.of(
                collegeSeeder,
                adminSeeder,
                pageantSeeder,
                judgeSeeder,
                phaseSeeder,
                segmentSeeder,
                criterionSeeder,
                candidateSeeder,
                candidateSegmentQualificationSeeder,
                scoreSeeder
        );
    }

    @Override
    public void run(String... args) {
        seeders.forEach(DatabaseSeeder::seed);
    }
}
