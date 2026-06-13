package com.gdgocbu.tabulation.backend.seeders;

import com.gdgocbu.tabulation.backend.models.College;
import com.gdgocbu.tabulation.backend.repositories.CollegeRepository;
import com.gdgocbu.tabulation.backend.utilities.CollegeCandidateData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CollegeSeeder implements DatabaseSeeder {
    @Autowired
    private CollegeRepository repository;

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
