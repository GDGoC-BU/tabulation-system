package com.michaelcanonizado.backend.mappers;

import com.michaelcanonizado.backend.dtos.candidate.CandidateSummaryDTO;
import com.michaelcanonizado.backend.dtos.college.CollegeCreateDTO;
import com.michaelcanonizado.backend.dtos.college.CollegeDetailedDTO;
import com.michaelcanonizado.backend.dtos.college.CollegeSummaryDTO;
import com.michaelcanonizado.backend.models.Candidate;
import com.michaelcanonizado.backend.models.College;
import com.michaelcanonizado.backend.models.Gender;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-27T19:56:05+0800",
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

        college.setCandidates( candidateSummaryDTOListToCandidateList( collegeDetailedDTO.candidates() ) );

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
        List<CandidateSummaryDTO> candidates = null;

        id = college.getId();
        code = college.getCode();
        name = college.getName();
        candidates = candidateListToCandidateSummaryDTOList( college.getCandidates() );

        CollegeDetailedDTO collegeDetailedDTO = new CollegeDetailedDTO( id, code, name, candidates );

        return collegeDetailedDTO;
    }

    @Override
    public void updateEntityFromDTO(College college, CollegeSummaryDTO collegeSummaryDTO) {
        if ( collegeSummaryDTO == null ) {
            return;
        }

        college.setCode( collegeSummaryDTO.code() );
        college.setName( collegeSummaryDTO.name() );
    }

    protected Candidate candidateSummaryDTOToCandidate(CandidateSummaryDTO candidateSummaryDTO) {
        if ( candidateSummaryDTO == null ) {
            return null;
        }

        int number = 0;
        String firstName = null;
        String lastName = null;
        Gender gender = null;
        int age = 0;

        number = candidateSummaryDTO.number();
        firstName = candidateSummaryDTO.firstName();
        lastName = candidateSummaryDTO.lastName();
        gender = candidateSummaryDTO.gender();
        age = candidateSummaryDTO.age();

        College college = null;

        Candidate candidate = new Candidate( number, firstName, lastName, gender, age, college );

        return candidate;
    }

    protected List<Candidate> candidateSummaryDTOListToCandidateList(List<CandidateSummaryDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<Candidate> list1 = new ArrayList<Candidate>( list.size() );
        for ( CandidateSummaryDTO candidateSummaryDTO : list ) {
            list1.add( candidateSummaryDTOToCandidate( candidateSummaryDTO ) );
        }

        return list1;
    }

    protected CandidateSummaryDTO candidateToCandidateSummaryDTO(Candidate candidate) {
        if ( candidate == null ) {
            return null;
        }

        UUID id = null;
        int number = 0;
        String firstName = null;
        String lastName = null;
        Gender gender = null;
        int age = 0;

        id = candidate.getId();
        number = candidate.getNumber();
        firstName = candidate.getFirstName();
        lastName = candidate.getLastName();
        gender = candidate.getGender();
        age = candidate.getAge();

        CandidateSummaryDTO candidateSummaryDTO = new CandidateSummaryDTO( id, number, firstName, lastName, gender, age );

        return candidateSummaryDTO;
    }

    protected List<CandidateSummaryDTO> candidateListToCandidateSummaryDTOList(List<Candidate> list) {
        if ( list == null ) {
            return null;
        }

        List<CandidateSummaryDTO> list1 = new ArrayList<CandidateSummaryDTO>( list.size() );
        for ( Candidate candidate : list ) {
            list1.add( candidateToCandidateSummaryDTO( candidate ) );
        }

        return list1;
    }
}
