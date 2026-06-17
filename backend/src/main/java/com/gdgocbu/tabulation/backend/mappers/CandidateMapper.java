package com.gdgocbu.tabulation.backend.mappers;

import com.gdgocbu.tabulation.backend.dtos.candidate.*;
import com.gdgocbu.tabulation.backend.models.Candidate;
import com.gdgocbu.tabulation.backend.services.CollegeService;
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
