package com.michaelcanonizado.backend.mapper;

import com.michaelcanonizado.backend.dtos.college.CollegeCreateDTO;
import com.michaelcanonizado.backend.dtos.college.CollegeDetailedDTO;
import com.michaelcanonizado.backend.dtos.college.CollegeSummaryDTO;
import com.michaelcanonizado.backend.models.Candidate;
import com.michaelcanonizado.backend.models.College;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-23T19:20:06+0800",
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
    public College toEntity(CollegeDetailedDTO collegeDetailedDTO) {
        if ( collegeDetailedDTO == null ) {
            return null;
        }

        String code = null;
        String name = null;

        code = collegeDetailedDTO.code();
        name = collegeDetailedDTO.name();

        College college = new College( code, name );

        List<Candidate> list = collegeDetailedDTO.candidates();
        if ( list != null ) {
            college.setCandidates( new ArrayList<Candidate>( list ) );
        }

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
    public CollegeDetailedDTO toDetailedDTO(College college) {
        if ( college == null ) {
            return null;
        }

        UUID id = null;
        String code = null;
        String name = null;
        List<Candidate> candidates = null;

        id = college.getId();
        code = college.getCode();
        name = college.getName();
        List<Candidate> list = college.getCandidates();
        if ( list != null ) {
            candidates = new ArrayList<Candidate>( list );
        }

        CollegeDetailedDTO collegeDetailedDTO = new CollegeDetailedDTO( id, code, name, candidates );

        return collegeDetailedDTO;
    }
}
