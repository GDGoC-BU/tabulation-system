package com.michaelcanonizado.backend.dtos.candidate;

import com.michaelcanonizado.backend.models.College;
import com.michaelcanonizado.backend.models.Gender;

import java.util.UUID;

public record CandidateCreateDTO(
        int number,
        String firstName,
        String lastName,
        Gender gender,
        int age,
        UUID collegeId
) {
}
