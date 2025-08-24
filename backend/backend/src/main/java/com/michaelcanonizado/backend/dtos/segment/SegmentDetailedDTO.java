package com.michaelcanonizado.backend.dtos.segment;

import com.michaelcanonizado.backend.dtos.candidate.CandidateSummaryDTO;
import com.michaelcanonizado.backend.dtos.criterion.CriterionSummaryDTO;

import java.util.List;
import java.util.UUID;

public record SegmentDetailedDTO(UUID id, String name, List<CriterionSummaryDTO> criteria, List<CandidateSummaryDTO> qualifiedCandidates) {
}
