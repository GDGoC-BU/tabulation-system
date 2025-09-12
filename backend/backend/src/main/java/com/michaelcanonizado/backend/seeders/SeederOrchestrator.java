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
            CandidateSeeder candidateSeeder,
            CandidateSegmentQualificationSeeder candidateSegmentQualificationSeeder,
            CollegeSeeder collegeSeeder,
            CriterionSeeder criterionSeeder,
            JudgeSeeder judgeSeeder,
            ManagerSeeder managerSeeder,
            PageantSeeder pageantSeeder,
            ScoreSeeder scoreSeeder,
            SegmentSeeder segmentSeeder
    ) {
        this.seeders = List.of(
                pageantSeeder,
                candidateSeeder,
                candidateSegmentQualificationSeeder,
                collegeSeeder,
                criterionSeeder,
                judgeSeeder,
                managerSeeder,
                scoreSeeder,
                segmentSeeder
        );
    }

    @Override
    public void run(String... args) {
        seeders.forEach(DatabaseSeeder::seed);
    }
}
