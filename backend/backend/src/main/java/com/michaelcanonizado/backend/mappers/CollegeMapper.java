package com.michaelcanonizado.backend.mappers;

import com.michaelcanonizado.backend.dtos.college.CollegeCreateDTO;
import com.michaelcanonizado.backend.dtos.college.CollegeHierarchyDTO;
import com.michaelcanonizado.backend.dtos.college.CollegeSummaryDTO;
import com.michaelcanonizado.backend.dtos.college.CollegeUpdateDTO;
import com.michaelcanonizado.backend.models.College;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CollegeMapper {
    College toEntity(CollegeCreateDTO collegeCreateDTO);
    College toEntity(CollegeSummaryDTO collegeSummaryDTO);
    CollegeSummaryDTO toSummaryDTO(College college);
    CollegeHierarchyDTO toHierarchyDTO(College college);

    void updateEntityFromDTO(@MappingTarget College college, CollegeUpdateDTO collegeUpdateDTO);
}
