package com.michaelcanonizado.backend.mapper;

import com.michaelcanonizado.backend.dtos.segment.SegmentCreateDTO;
import com.michaelcanonizado.backend.dtos.segment.SegmentDetailedDTO;
import com.michaelcanonizado.backend.dtos.segment.SegmentSummaryDTO;
import com.michaelcanonizado.backend.models.Segment;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SegmentMapper {
    Segment toEntity(SegmentCreateDTO segmentCreateDTO);
    Segment toEntity(SegmentSummaryDTO segmentSummaryDTO);
    SegmentSummaryDTO toSummaryDTO(Segment segment);
    @Mapping(target = "qualifiedCandidates", source = "qualifiedCandidates")
    SegmentDetailedDTO toDetailedDTO(Segment segment);

    void updateEntityFromDTO(@MappingTarget Segment segment, SegmentSummaryDTO segmentSummaryDTO);

    @AfterMapping
    default void linkCriteria(@MappingTarget Segment segment) {
        if (segment.getCriteria() != null) {
            segment.getCriteria().forEach(criterion -> criterion.setSegment(segment));
        }
    }
}
