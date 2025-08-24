package com.michaelcanonizado.backend.mapper;

import com.michaelcanonizado.backend.dtos.segment.SegmentCreateDTO;
import com.michaelcanonizado.backend.dtos.segment.SegmentDetailedDTO;
import com.michaelcanonizado.backend.dtos.segment.SegmentSummaryDTO;
import com.michaelcanonizado.backend.models.Segment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SegmentMapper {
    Segment toEntity(SegmentCreateDTO segmentCreateDTO);
    Segment toEntity(SegmentSummaryDTO segmentSummaryDTO);
    SegmentSummaryDTO toSummaryDTO(Segment segment);
    SegmentDetailedDTO toDetailedDTO(Segment segment);
}
