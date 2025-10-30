package com.michaelcanonizado.backend.dtos.candidate;

import com.michaelcanonizado.backend.models.CandidateGender;

import java.util.UUID;

public record CandidateHierarchyDTO(
        UUID id,
        int number,
        String firstName,
        String lastName,
        CandidateGender gender,
        int age
) {
}
