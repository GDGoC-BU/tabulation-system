package com.michaelcanonizado.backend.seeders;

import com.michaelcanonizado.backend.models.College;
import com.michaelcanonizado.backend.repositories.CollegeRepository;
import com.michaelcanonizado.backend.utilities.CollegeCandidateData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CollegeSeeder implements DatabaseSeeder {
    private final CollegeRepository repository;

    @Autowired
    public CollegeSeeder(CollegeRepository repository) {
        this.repository = repository;
    }

    @Override
    public void seed() {
        List<College> colleges = new ArrayList<>();
        for (CollegeCandidateData.CollegeTemp collegeTemp : CollegeCandidateData.colleges) {
            colleges.add(
                    new College(
                            collegeTemp.getCode(),
                            collegeTemp.getName()
                    )
            );
        }
        repository.saveAll(colleges);
    }
}
