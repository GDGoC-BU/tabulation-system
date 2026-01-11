package com.michaelcanonizado.backend.mappers;

import com.michaelcanonizado.backend.dtos.segment.*;
import com.michaelcanonizado.backend.models.Segment;
import com.michaelcanonizado.backend.services.PhaseService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(
        componentModel = "spring",
        uses = {
                PhaseService.class,
                CriterionMapper.class
        }
)
public interface SegmentMapper {
    @Mapping(target = "phase", source = "phaseId")
    Segment toEntity(SegmentCreateDTO segmentCreateDTO);

    SegmentSummaryDTO toSummaryDTO(Segment segment);

    SegmentDetailedDTO toDetailedDTO(Segment segment);

    SegmentBreakdownDTO toBreakdownDTO(Segment segment);

    SegmentHierarchyDTO toHierarchyDTO(Segment segment);

    void updateEntityFromDTO(@MappingTarget Segment segment, SegmentUpdateDTO segmentUpdateDTO);
}
