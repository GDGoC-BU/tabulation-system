package com.gdgocbu.tabulation.backend.dtos.candidate;

import com.gdgocbu.tabulation.backend.dtos.college.CollegeHierarchyDTO;
import com.gdgocbu.tabulation.backend.models.CandidateGender;

import java.util.UUID;

public record CandidateHierarchyDTO(
        UUID id,
        int number,
        String firstName,
        String lastName,
        CandidateGender gender,
        CollegeHierarchyDTO college,
        int age
) {
}
