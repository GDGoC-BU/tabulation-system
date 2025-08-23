package com.michaelcanonizado.backend.dtos.college;

import com.michaelcanonizado.backend.dtos.candidate.CandidateSummaryDTO;

import java.util.List;
import java.util.UUID;

public record CollegeDetailedDTO(UUID id, String code, String name, List<CandidateSummaryDTO> candidates) {
}
