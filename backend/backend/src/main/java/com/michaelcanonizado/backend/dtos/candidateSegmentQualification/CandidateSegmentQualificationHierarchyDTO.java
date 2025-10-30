package com.michaelcanonizado.backend.dtos.candidateSegmentQualification;

import com.michaelcanonizado.backend.dtos.candidate.CandidateHierarchyDTO;

import java.util.UUID;

public record CandidateSegmentQualificationHierarchyDTO(
        UUID id,
        CandidateHierarchyDTO candidate,
        boolean isQualified,
        Double score
) {
}
