package com.michaelcanonizado.backend.dtos.segment;

import com.michaelcanonizado.backend.dtos.candidate.CandidateDetailedDTO;
import com.michaelcanonizado.backend.dtos.criterion.CriterionSummaryDTO;
import com.michaelcanonizado.backend.dtos.phase.PhaseSummaryDTO;
import com.michaelcanonizado.backend.models.PhaseSegmentStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record SegmentDetailedDTO(
        UUID id,
        String name,
        int sequence,
        Integer candidateLimit,
        String formula,
        PhaseSegmentStatus status,
        PhaseSummaryDTO phase,
        List<CriterionSummaryDTO> criteria,
        List<CandidateDetailedDTO> qualifiedCandidates
) {
}
