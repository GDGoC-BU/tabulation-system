package com.michaelcanonizado.backend.mapper;

import com.michaelcanonizado.backend.dtos.candidate.CandidateCreateDTO;
import com.michaelcanonizado.backend.dtos.candidate.CandidateSummaryDTO;
import com.michaelcanonizado.backend.models.Candidate;
import com.michaelcanonizado.backend.models.College;
import com.michaelcanonizado.backend.models.Gender;
import com.michaelcanonizado.backend.services.CollegeService;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-24T21:19:32+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Oracle Corporation)"
)
@Component
public class CandidateMapperImpl implements CandidateMapper {

    @Autowired
    private CollegeService collegeService;

    @Override
    public Candidate toEntity(CandidateCreateDTO candidateCreateDTO) {
        if ( candidateCreateDTO == null ) {
            return null;
        }

        College college = null;
        int number = 0;
        String firstName = null;
        String lastName = null;
        Gender gender = null;
        int age = 0;

        college = collegeService.findById( candidateCreateDTO.collegeId() );
        number = candidateCreateDTO.number();
        firstName = candidateCreateDTO.firstName();
        lastName = candidateCreateDTO.lastName();
        gender = candidateCreateDTO.gender();
        age = candidateCreateDTO.age();

        Candidate candidate = new Candidate( number, firstName, lastName, gender, age, college );

        return candidate;
    }

    @Override
    public CandidateSummaryDTO toSummaryDTO(Candidate candidate) {
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
}
