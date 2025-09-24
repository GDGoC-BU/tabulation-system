package com.michaelcanonizado.backend.mappers;

import com.michaelcanonizado.backend.dtos.college.CollegeCreateDTO;
import com.michaelcanonizado.backend.dtos.college.CollegeSummaryDTO;
import com.michaelcanonizado.backend.dtos.college.CollegeUpdateDTO;
import com.michaelcanonizado.backend.models.College;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-24T23:19:21+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Oracle Corporation)"
)
@Component
public class CollegeMapperImpl implements CollegeMapper {

    @Override
    public College toEntity(CollegeCreateDTO collegeCreateDTO) {
        if ( collegeCreateDTO == null ) {
            return null;
        }

        String code = null;
        String name = null;

        code = collegeCreateDTO.code();
        name = collegeCreateDTO.name();

        College college = new College( code, name );

        return college;
    }

    @Override
    public College toEntity(CollegeSummaryDTO collegeSummaryDTO) {
        if ( collegeSummaryDTO == null ) {
            return null;
        }

        String code = null;
        String name = null;

        code = collegeSummaryDTO.code();
        name = collegeSummaryDTO.name();

        College college = new College( code, name );

        return college;
    }

    @Override
    public CollegeSummaryDTO toSummaryDTO(College college) {
        if ( college == null ) {
            return null;
        }

        UUID id = null;
        String code = null;
        String name = null;

        id = college.getId();
        code = college.getCode();
        name = college.getName();

        CollegeSummaryDTO collegeSummaryDTO = new CollegeSummaryDTO( id, code, name );

        return collegeSummaryDTO;
    }

    @Override
    public void updateEntityFromDTO(College college, CollegeUpdateDTO collegeUpdateDTO) {
        if ( collegeUpdateDTO == null ) {
            return;
        }

        college.setCode( collegeUpdateDTO.code() );
        college.setName( collegeUpdateDTO.name() );
    }
}
