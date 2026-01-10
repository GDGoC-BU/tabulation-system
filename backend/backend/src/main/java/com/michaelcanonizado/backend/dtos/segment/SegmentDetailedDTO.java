package com.michaelcanonizado.backend.dtos.segment;

import com.michaelcanonizado.backend.dtos.candidateSegmentQualification.CandidateSegmentQualificationSummaryDTO;
import com.michaelcanonizado.backend.dtos.criterion.CriterionSummaryDTO;
import com.michaelcanonizado.backend.dtos.phase.PhaseSummaryDTO;
import com.michaelcanonizado.backend.models.Formula;
import com.michaelcanonizado.backend.models.PhaseSegmentStatus;

import java.util.List;
import java.util.UUID;

public record SegmentDetailedDTO(
        UUID id,
        String name,
        int sequence,
        Integer candidateLimit,
        Formula formula,
        PhaseSegmentStatus status,
        PhaseSummaryDTO phase,
        List<CriterionSummaryDTO> criteria,
        List<CandidateSegmentQualificationSummaryDTO> candidateQualifications
) {
}
