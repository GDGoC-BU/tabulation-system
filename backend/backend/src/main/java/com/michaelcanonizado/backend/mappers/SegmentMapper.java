package com.michaelcanonizado.backend.mappers;

import com.michaelcanonizado.backend.dtos.segment.SegmentCreateDTO;
import com.michaelcanonizado.backend.dtos.segment.SegmentDetailedDTO;
import com.michaelcanonizado.backend.dtos.segment.SegmentSummaryDTO;
import com.michaelcanonizado.backend.dtos.segment.SegmentUpdateDTO;
import com.michaelcanonizado.backend.models.Segment;
import com.michaelcanonizado.backend.services.PhaseService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {PhaseService.class})
public interface SegmentMapper {
    @Mapping(target = "phase", source = "phaseId")
    Segment toEntity(SegmentCreateDTO segmentCreateDTO);
    Segment toEntity(SegmentSummaryDTO segmentSummaryDTO);
    SegmentSummaryDTO toSummaryDTO(Segment segment);
    @Mapping(target = "qualifiedCandidates", source = "qualifiedCandidates")
    SegmentDetailedDTO toDetailedDTO(Segment segment);

    void updateEntityFromDTO(@MappingTarget Segment segment, SegmentUpdateDTO segmentUpdateDTO);
}
