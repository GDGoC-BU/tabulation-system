package com.michaelcanonizado.backend.mapper;

import com.michaelcanonizado.backend.dtos.college.CollegeCreateDTO;
import com.michaelcanonizado.backend.dtos.college.CollegeDetailedDTO;
import com.michaelcanonizado.backend.dtos.college.CollegeSummaryDTO;
import com.michaelcanonizado.backend.models.College;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CollegeMapper {
    College toEntity(CollegeCreateDTO collegeCreateDTO);
    College toEntity(CollegeDetailedDTO collegeDetailedDTO);
    College toEntity(CollegeSummaryDTO collegeSummaryDTO);
    CollegeSummaryDTO toSummaryDTO(College college);
    CollegeDetailedDTO toDetailedDTO(College college);
}
