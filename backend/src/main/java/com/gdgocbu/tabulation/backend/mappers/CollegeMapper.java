package com.gdgocbu.tabulation.backend.mappers;

import com.gdgocbu.tabulation.backend.dtos.college.CollegeCreateDTO;
import com.gdgocbu.tabulation.backend.dtos.college.CollegeHierarchyDTO;
import com.gdgocbu.tabulation.backend.dtos.college.CollegeSummaryDTO;
import com.gdgocbu.tabulation.backend.dtos.college.CollegeUpdateDTO;
import com.gdgocbu.tabulation.backend.models.College;
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
