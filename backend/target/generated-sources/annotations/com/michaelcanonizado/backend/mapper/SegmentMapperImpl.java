package com.michaelcanonizado.backend.mapper;

import com.michaelcanonizado.backend.dtos.segment.SegmentCreateDTO;
import com.michaelcanonizado.backend.dtos.segment.SegmentSummaryDTO;
import com.michaelcanonizado.backend.models.Segment;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-23T22:46:33+0800",
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
}
