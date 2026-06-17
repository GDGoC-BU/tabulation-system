package com.gdgocbu.tabulation.backend.dtos.candidateSegmentQualification;

import com.gdgocbu.tabulation.backend.dtos.candidate.CandidateHierarchyDTO;

import java.util.UUID;

public record CandidateSegmentQualificationHierarchyDTO(
        UUID id,
        Integer rank,
        CandidateHierarchyDTO candidate,
        boolean isQualified,
        boolean isTied,
        Double score
) {
}
