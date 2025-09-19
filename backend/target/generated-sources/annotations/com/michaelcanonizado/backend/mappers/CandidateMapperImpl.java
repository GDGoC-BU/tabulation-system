package com.michaelcanonizado.backend.mappers;

import com.michaelcanonizado.backend.dtos.candidate.CandidateCreateDTO;
import com.michaelcanonizado.backend.dtos.candidate.CandidateSummaryDTO;
import com.michaelcanonizado.backend.dtos.candidate.CandidateUpdateDTO;
import com.michaelcanonizado.backend.models.Candidate;
import com.michaelcanonizado.backend.models.CandidateGender;
import com.michaelcanonizado.backend.models.College;
import com.michaelcanonizado.backend.models.Pageant;
import com.michaelcanonizado.backend.services.CollegeService;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-19T19:10:07+0800",
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
        CandidateGender candidateGender = null;
        int age = 0;

        college = collegeService.findById( candidateCreateDTO.collegeId() );
        number = candidateCreateDTO.number();
        firstName = candidateCreateDTO.firstName();
        lastName = candidateCreateDTO.lastName();
        candidateGender = candidateCreateDTO.candidateGender();
        age = candidateCreateDTO.age();

        Pageant pageant = null;

        Candidate candidate = new Candidate( number, firstName, lastName, candidateGender, age, college, pageant );

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
        CandidateGender candidateGender = null;
        int age = 0;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        id = candidate.getId();
        number = candidate.getNumber();
        firstName = candidate.getFirstName();
        lastName = candidate.getLastName();
        candidateGender = candidate.getCandidateGender();
        age = candidate.getAge();
        createdAt = candidate.getCreatedAt();
        updatedAt = candidate.getUpdatedAt();

        CandidateSummaryDTO candidateSummaryDTO = new CandidateSummaryDTO( id, number, firstName, lastName, candidateGender, age, createdAt, updatedAt );

        return candidateSummaryDTO;
    }

    @Override
    public void updateEntityFromDTO(Candidate candidate, CandidateUpdateDTO candidateUpdateDTO) {
        if ( candidateUpdateDTO == null ) {
            return;
        }

        candidate.setNumber( candidateUpdateDTO.number() );
        candidate.setFirstName( candidateUpdateDTO.firstName() );
        candidate.setLastName( candidateUpdateDTO.lastName() );
        candidate.setCandidateGender( candidateUpdateDTO.candidateGender() );
        candidate.setAge( candidateUpdateDTO.age() );
    }
}
