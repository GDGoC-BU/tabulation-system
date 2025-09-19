package com.michaelcanonizado.backend.mappers;

import com.michaelcanonizado.backend.dtos.candidate.CandidateSummaryDTO;
import com.michaelcanonizado.backend.dtos.criterion.CriterionSummaryDTO;
import com.michaelcanonizado.backend.dtos.phase.PhaseSummaryDTO;
import com.michaelcanonizado.backend.dtos.segment.SegmentCreateDTO;
import com.michaelcanonizado.backend.dtos.segment.SegmentDetailedDTO;
import com.michaelcanonizado.backend.dtos.segment.SegmentSummaryDTO;
import com.michaelcanonizado.backend.dtos.segment.SegmentUpdateDTO;
import com.michaelcanonizado.backend.models.Candidate;
import com.michaelcanonizado.backend.models.CandidateGender;
import com.michaelcanonizado.backend.models.Criterion;
import com.michaelcanonizado.backend.models.Pageant;
import com.michaelcanonizado.backend.models.Phase;
import com.michaelcanonizado.backend.models.PhaseSegmentStatus;
import com.michaelcanonizado.backend.models.Segment;
import com.michaelcanonizado.backend.services.PhaseService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-19T17:28:51+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Oracle Corporation)"
)
@Component
public class SegmentMapperImpl implements SegmentMapper {

    @Autowired
    private PhaseService phaseService;

    @Override
    public Segment toEntity(SegmentCreateDTO segmentCreateDTO) {
        if ( segmentCreateDTO == null ) {
            return null;
        }

        Phase phase = null;
        String name = null;
        int sequence = 0;

        phase = phaseService.findById( segmentCreateDTO.phaseId() );
        name = segmentCreateDTO.name();
        sequence = segmentCreateDTO.sequence();

        Segment segment = new Segment( name, sequence, phase );

        return segment;
    }

    @Override
    public Segment toEntity(SegmentSummaryDTO segmentSummaryDTO) {
        if ( segmentSummaryDTO == null ) {
            return null;
        }

        String name = null;
        int sequence = 0;
        Phase phase = null;

        name = segmentSummaryDTO.name();
        sequence = segmentSummaryDTO.sequence();
        phase = phaseSummaryDTOToPhase( segmentSummaryDTO.phase() );

        Segment segment = new Segment( name, sequence, phase );

        segment.setStatus( segmentSummaryDTO.status() );

        return segment;
    }

    @Override
    public SegmentSummaryDTO toSummaryDTO(Segment segment) {
        if ( segment == null ) {
            return null;
        }

        UUID id = null;
        String name = null;
        int sequence = 0;
        PhaseSegmentStatus status = null;
        PhaseSummaryDTO phase = null;

        id = segment.getId();
        name = segment.getName();
        sequence = segment.getSequence();
        status = segment.getStatus();
        phase = phaseToPhaseSummaryDTO( segment.getPhase() );

        SegmentSummaryDTO segmentSummaryDTO = new SegmentSummaryDTO( id, name, sequence, status, phase );

        return segmentSummaryDTO;
    }

    @Override
    public SegmentDetailedDTO toDetailedDTO(Segment segment) {
        if ( segment == null ) {
            return null;
        }

        List<CandidateSummaryDTO> qualifiedCandidates = null;
        UUID id = null;
        String name = null;
        int sequence = 0;
        PhaseSegmentStatus status = null;
        PhaseSummaryDTO phase = null;
        List<CriterionSummaryDTO> criteria = null;

        qualifiedCandidates = candidateListToCandidateSummaryDTOList( segment.getQualifiedCandidates() );
        id = segment.getId();
        name = segment.getName();
        sequence = segment.getSequence();
        status = segment.getStatus();
        phase = phaseToPhaseSummaryDTO( segment.getPhase() );
        criteria = criterionListToCriterionSummaryDTOList( segment.getCriteria() );

        SegmentDetailedDTO segmentDetailedDTO = new SegmentDetailedDTO( id, name, sequence, status, phase, criteria, qualifiedCandidates );

        return segmentDetailedDTO;
    }

    @Override
    public void updateEntityFromDTO(Segment segment, SegmentUpdateDTO segmentUpdateDTO) {
        if ( segmentUpdateDTO == null ) {
            return;
        }

        segment.setName( segmentUpdateDTO.name() );
        segment.setSequence( segmentUpdateDTO.sequence() );
        segment.setStatus( segmentUpdateDTO.status() );
    }

    protected Phase phaseSummaryDTOToPhase(PhaseSummaryDTO phaseSummaryDTO) {
        if ( phaseSummaryDTO == null ) {
            return null;
        }

        String name = null;
        int sequence = 0;

        name = phaseSummaryDTO.name();
        sequence = phaseSummaryDTO.sequence();

        Pageant pageant = null;

        Phase phase = new Phase( name, sequence, pageant );

        return phase;
    }

    protected PhaseSummaryDTO phaseToPhaseSummaryDTO(Phase phase) {
        if ( phase == null ) {
            return null;
        }

        UUID id = null;
        String name = null;
        int sequence = 0;

        id = phase.getId();
        name = phase.getName();
        sequence = phase.getSequence();

        PhaseSummaryDTO phaseSummaryDTO = new PhaseSummaryDTO( id, name, sequence );

        return phaseSummaryDTO;
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

    protected CriterionSummaryDTO criterionToCriterionSummaryDTO(Criterion criterion) {
        if ( criterion == null ) {
            return null;
        }

        UUID id = null;
        String name = null;
        int maxScore = 0;

        id = criterion.getId();
        name = criterion.getName();
        maxScore = criterion.getMaxScore();

        CriterionSummaryDTO criterionSummaryDTO = new CriterionSummaryDTO( id, name, maxScore );

        return criterionSummaryDTO;
    }

    protected List<CriterionSummaryDTO> criterionListToCriterionSummaryDTOList(List<Criterion> list) {
        if ( list == null ) {
            return null;
        }

        List<CriterionSummaryDTO> list1 = new ArrayList<CriterionSummaryDTO>( list.size() );
        for ( Criterion criterion : list ) {
            list1.add( criterionToCriterionSummaryDTO( criterion ) );
        }

        return list1;
    }
}
