package com.gdgocbu.tabulation.backend.dtos.candidateSegmentQualification;

import com.gdgocbu.tabulation.backend.dtos.candidate.CandidateSummaryDTO;
import com.gdgocbu.tabulation.backend.models.CriteriaBreakdown;

import java.util.List;
import java.util.UUID;

public record CandidateSegmentQualificationSummaryDTO(
        UUID id,
        Integer rank,
        CandidateSummaryDTO candidate,
        boolean isQualified,
        boolean isTied,
        Double score,
        List<CriteriaBreakdown> criteriaBreakdown
) {
}
