package com.michaelcanonizado.backend.mappers;

import com.michaelcanonizado.backend.dtos.phase.PhaseCreateDTO;
import com.michaelcanonizado.backend.dtos.phase.PhaseDetailedDTO;
import com.michaelcanonizado.backend.dtos.phase.PhaseSummaryDTO;
import com.michaelcanonizado.backend.dtos.phase.PhaseUpdateDTO;
import com.michaelcanonizado.backend.dtos.segment.SegmentSummaryDTO;
import com.michaelcanonizado.backend.models.Pageant;
import com.michaelcanonizado.backend.models.Phase;
import com.michaelcanonizado.backend.models.PhaseSegmentStatus;
import com.michaelcanonizado.backend.models.Segment;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-19T15:40:55+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Oracle Corporation)"
)
@Component
public class PhaseMapperImpl implements PhaseMapper {

    @Override
    public Phase toEntity(PhaseCreateDTO phaseCreateDTO) {
        if ( phaseCreateDTO == null ) {
            return null;
        }

        String name = null;
        int sequence = 0;

        name = phaseCreateDTO.name();
        sequence = phaseCreateDTO.sequence();

        Pageant pageant = null;

        Phase phase = new Phase( name, sequence, pageant );

        return phase;
    }

    @Override
    public PhaseSummaryDTO toSummaryDTO(Phase phase) {
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

    @Override
    public PhaseDetailedDTO toDetailedDTO(Phase phase) {
        if ( phase == null ) {
            return null;
        }

        UUID id = null;
        String name = null;
        int sequence = 0;
        List<SegmentSummaryDTO> segments = null;

        id = phase.getId();
        name = phase.getName();
        sequence = phase.getSequence();
        segments = segmentListToSegmentSummaryDTOList( phase.getSegments() );

        PhaseDetailedDTO phaseDetailedDTO = new PhaseDetailedDTO( id, name, sequence, segments );

        return phaseDetailedDTO;
    }

    @Override
    public void updateEntityFromDTO(Phase phase, PhaseUpdateDTO phaseUpdateDTO) {
        if ( phaseUpdateDTO == null ) {
            return;
        }

        phase.setName( phaseUpdateDTO.name() );
        phase.setSequence( phaseUpdateDTO.sequence() );
    }

    protected SegmentSummaryDTO segmentToSegmentSummaryDTO(Segment segment) {
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
        phase = toSummaryDTO( segment.getPhase() );

        SegmentSummaryDTO segmentSummaryDTO = new SegmentSummaryDTO( id, name, sequence, status, phase );

        return segmentSummaryDTO;
    }

    protected List<SegmentSummaryDTO> segmentListToSegmentSummaryDTOList(List<Segment> list) {
        if ( list == null ) {
            return null;
        }

        List<SegmentSummaryDTO> list1 = new ArrayList<SegmentSummaryDTO>( list.size() );
        for ( Segment segment : list ) {
            list1.add( segmentToSegmentSummaryDTO( segment ) );
        }

        return list1;
    }
}
