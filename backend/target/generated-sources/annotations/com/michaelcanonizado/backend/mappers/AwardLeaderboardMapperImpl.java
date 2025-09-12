package com.michaelcanonizado.backend.mappers;

import com.michaelcanonizado.backend.dtos.AwardLeaderboardSummaryDTO;
import com.michaelcanonizado.backend.dtos.candidate.CandidateSummaryDTO;
import com.michaelcanonizado.backend.models.AwardLeaderboard;
import com.michaelcanonizado.backend.models.Candidate;
import com.michaelcanonizado.backend.models.CandidateGender;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-13T00:35:34+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Oracle Corporation)"
)
@Component
public class AwardLeaderboardMapperImpl implements AwardLeaderboardMapper {

    @Override
    public AwardLeaderboardSummaryDTO toSummaryDTO(AwardLeaderboard awardLeaderboard) {
        if ( awardLeaderboard == null ) {
            return null;
        }

        UUID id = null;
        CandidateSummaryDTO candidate = null;
        Double score = null;

        id = awardLeaderboard.getId();
        candidate = candidateToCandidateSummaryDTO( awardLeaderboard.getCandidate() );
        score = awardLeaderboard.getScore();

        AwardLeaderboardSummaryDTO awardLeaderboardSummaryDTO = new AwardLeaderboardSummaryDTO( id, candidate, score );

        return awardLeaderboardSummaryDTO;
    }

    protected CandidateSummaryDTO candidateToCandidateSummaryDTO(Candidate candidate) {
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
}
