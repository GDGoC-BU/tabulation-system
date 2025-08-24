package com.michaelcanonizado.backend.mapper;

import com.michaelcanonizado.backend.dtos.candidate.CandidateSummaryDTO;
import com.michaelcanonizado.backend.dtos.criterion.CriterionSummaryDTO;
import com.michaelcanonizado.backend.dtos.segment.SegmentCreateDTO;
import com.michaelcanonizado.backend.dtos.segment.SegmentDetailedDTO;
import com.michaelcanonizado.backend.dtos.segment.SegmentSummaryDTO;
import com.michaelcanonizado.backend.models.Criterion;
import com.michaelcanonizado.backend.models.Segment;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-24T19:26:15+0800",
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

        return segment;
    }

    @Override
    public SegmentSummaryDTO toSummaryDTO(Segment segment) {
        if ( segment == null ) {
            return null;
        }

        UUID id = null;
        String name = null;

        id = segment.getId();
        name = segment.getName();

        SegmentSummaryDTO segmentSummaryDTO = new SegmentSummaryDTO( id, name );

        return segmentSummaryDTO;
    }

    @Override
    public SegmentDetailedDTO toDetailedDTO(Segment segment) {
        if ( segment == null ) {
            return null;
        }

        UUID id = null;
        String name = null;
        List<CriterionSummaryDTO> criteria = null;

        id = segment.getId();
        name = segment.getName();
        criteria = criterionListToCriterionSummaryDTOList( segment.getCriteria() );

        List<CandidateSummaryDTO> qualifiedCandidates = null;

        SegmentDetailedDTO segmentDetailedDTO = new SegmentDetailedDTO( id, name, criteria, qualifiedCandidates );

        return segmentDetailedDTO;
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
