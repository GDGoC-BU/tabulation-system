package com.gdgocbu.tabulation.backend.mappers;

import com.gdgocbu.tabulation.backend.dtos.segment.*;
import com.gdgocbu.tabulation.backend.models.Segment;
import com.gdgocbu.tabulation.backend.services.PhaseService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(
        componentModel = "spring",
        uses = {
                PhaseService.class,
                CriterionMapper.class,
                LeaderboardMapper.class
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
