package com.michaelcanonizado.backend.mappers;

import com.michaelcanonizado.backend.dtos.candidateSegmentQualification.CandidateSegmentQualificationHierarchyDTO;
import com.michaelcanonizado.backend.dtos.candidateSegmentQualification.CandidateSegmentQualificationSummaryDTO;
import com.michaelcanonizado.backend.models.CandidateSegmentQualification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CandidateSegmentQualificationMapper {
    @Mapping(target = "isQualified", source = "qualified")
    CandidateSegmentQualificationSummaryDTO toSummaryDTO(CandidateSegmentQualification candidateSegmentQualification);

    @Mapping(target = "isQualified", source = "qualified")
    CandidateSegmentQualificationHierarchyDTO toHierarchyDTO(CandidateSegmentQualification candidateSegmentQualification);
}
