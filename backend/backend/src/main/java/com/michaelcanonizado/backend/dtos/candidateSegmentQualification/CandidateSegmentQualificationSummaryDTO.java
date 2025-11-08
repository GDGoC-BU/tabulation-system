package com.michaelcanonizado.backend.dtos.candidateSegmentQualification;

import com.michaelcanonizado.backend.dtos.candidate.CandidateSummaryDTO;
import com.michaelcanonizado.backend.models.CriteriaBreakdown;

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
