package com.michaelcanonizado.backend.mappers;

import com.michaelcanonizado.backend.dtos.candidate.*;
import com.michaelcanonizado.backend.models.Candidate;
import com.michaelcanonizado.backend.services.CollegeService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(
        componentModel = "spring",
        uses = {
                CollegeService.class,
                CollegeMapper.class
        }
)
public interface CandidateMapper {
    @Mapping(target = "college", source = "collegeId")
    Candidate toEntity(CandidateCreateDTO candidateCreateDTO);

    CandidateSummaryDTO toSummaryDTO(Candidate candidate);
    CandidateHierarchyDTO toHierarchyDTO(Candidate candidate);

    void updateEntityFromDTO(@MappingTarget Candidate candidate, CandidateUpdateDTO candidateUpdateDTO);
}
