package com.michaelcanonizado.backend.dtos.candidate;

import com.michaelcanonizado.backend.models.Gender;

import java.util.UUID;

public record CandidateUpdateDTO(
        int number,
        String firstName,
        String lastName,
        Gender gender,
        int age
) {
}
