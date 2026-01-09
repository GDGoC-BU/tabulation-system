package com.michaelcanonizado.backend.dtos.segment;

import com.michaelcanonizado.backend.dtos.candidateSegmentQualification.CandidateSegmentQualificationHierarchyDTO;
import com.michaelcanonizado.backend.dtos.criterion.CriterionHierarchyDTO;
import com.michaelcanonizado.backend.models.Formula;
import com.michaelcanonizado.backend.models.PhaseSegmentStatus;

import java.util.List;
import java.util.UUID;

public record SegmentHierarchyDTO(
        UUID id,
        String name,
        int sequence,
        Integer candidateLimit,
        Formula formula,
        PhaseSegmentStatus status,
        List<CriterionHierarchyDTO> criteria,
        List<CandidateSegmentQualificationHierarchyDTO> candidateQualifications
) {
}
