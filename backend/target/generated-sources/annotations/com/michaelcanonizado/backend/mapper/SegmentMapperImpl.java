package com.michaelcanonizado.backend.mapper;

import com.michaelcanonizado.backend.dtos.candidate.CandidateSummaryDTO;
import com.michaelcanonizado.backend.dtos.criterion.CriterionSummaryDTO;
import com.michaelcanonizado.backend.dtos.segment.SegmentCreateDTO;
import com.michaelcanonizado.backend.dtos.segment.SegmentDetailedDTO;
import com.michaelcanonizado.backend.dtos.segment.SegmentSummaryDTO;
import com.michaelcanonizado.backend.models.Candidate;
import com.michaelcanonizado.backend.models.Criterion;
import com.michaelcanonizado.backend.models.Gender;
import com.michaelcanonizado.backend.models.Segment;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-25T00:41:41+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Oracle Corporation)"
)
@Component
public class SegmentMapperImpl implements SegmentMapper {

    @Override
    public Segment toEntity(SegmentCreateDTO segmentCreateDTO) {
        if ( segmentCreateDTO == null ) {
            return null;
        }

        String name = null;

        name = segmentCreateDTO.name();

        Segment segment = new Segment( name );

        return segment;
    }

    @Override
    public Segment toEntity(SegmentSummaryDTO segmentSummaryDTO) {
        if ( segmentSummaryDTO == null ) {
            return null;
        }

        String name = null;

        name = segmentSummaryDTO.name();

        Segment segment = new Segment( name );

        segment.setCriteria( criterionSummaryDTOListToCriterionList( segmentSummaryDTO.criteria() ) );

        return segment;
    }

    @Override
    public SegmentSummaryDTO toSummaryDTO(Segment segment) {
        if ( segment == null ) {
            return null;
        }

        UUID id = null;
        String name = null;
        List<CriterionSummaryDTO> criteria = null;

        id = segment.getId();
        name = segment.getName();
        criteria = criterionListToCriterionSummaryDTOList( segment.getCriteria() );

        SegmentSummaryDTO segmentSummaryDTO = new SegmentSummaryDTO( id, name, criteria );

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
        List<CriterionSummaryDTO> criteria = null;

        qualifiedCandidates = candidateListToCandidateSummaryDTOList( segment.getQualifiedCandidates() );
        id = segment.getId();
        name = segment.getName();
        criteria = criterionListToCriterionSummaryDTOList( segment.getCriteria() );

        SegmentDetailedDTO segmentDetailedDTO = new SegmentDetailedDTO( id, name, criteria, qualifiedCandidates );

        return segmentDetailedDTO;
    }

    @Override
    public void updateEntityFromDTO(Segment segment, SegmentSummaryDTO segmentSummaryDTO) {
        if ( segmentSummaryDTO == null ) {
            return;
        }

        segment.setName( segmentSummaryDTO.name() );
        if ( segment.getCriteria() != null ) {
            List<Criterion> list = criterionSummaryDTOListToCriterionList( segmentSummaryDTO.criteria() );
            if ( list != null ) {
                segment.getCriteria().clear();
                segment.getCriteria().addAll( list );
            }
            else {
                segment.setCriteria( null );
            }
        }
        else {
            List<Criterion> list = criterionSummaryDTOListToCriterionList( segmentSummaryDTO.criteria() );
            if ( list != null ) {
                segment.setCriteria( list );
            }
        }
    }

    protected Criterion criterionSummaryDTOToCriterion(CriterionSummaryDTO criterionSummaryDTO) {
        if ( criterionSummaryDTO == null ) {
            return null;
        }

        String name = null;
        int maxScore = 0;

        name = criterionSummaryDTO.name();
        maxScore = criterionSummaryDTO.maxScore();

        Segment segment = null;

        Criterion criterion = new Criterion( name, maxScore, segment );

        return criterion;
    }

    protected List<Criterion> criterionSummaryDTOListToCriterionList(List<CriterionSummaryDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<Criterion> list1 = new ArrayList<Criterion>( list.size() );
        for ( CriterionSummaryDTO criterionSummaryDTO : list ) {
            list1.add( criterionSummaryDTOToCriterion( criterionSummaryDTO ) );
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
