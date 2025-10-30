package com.michaelcanonizado.backend.mappers;

import com.michaelcanonizado.backend.dtos.segment.*;
import com.michaelcanonizado.backend.models.Segment;
import com.michaelcanonizado.backend.services.PhaseService;
import com.michaelcanonizado.backend.utilities.FormulaEncoder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(
        componentModel = "spring",
        uses = {
                PhaseService.class,
                FormulaEncoder.class,
                CriterionMapper.class,
                CandidateSegmentQualificationMapper.class
        }
)
public interface SegmentMapper {
    @Mapping(target = "phase", source = "phaseId")
    @Mapping(target = "formula", source = "formula", qualifiedByName = "encodeFormula")
    Segment toEntity(SegmentCreateDTO segmentCreateDTO);

    @Mapping(target = "formula", source = "formula", qualifiedByName = "decodeFormula")
    SegmentSummaryDTO toSummaryDTO(Segment segment);

    @Mapping(target = "formula", source = "formula", qualifiedByName = "decodeFormula")
    SegmentDetailedDTO toDetailedDTO(Segment segment);

    SegmentBreakdownDTO toBreakdownDTO(Segment segment);

    @Mapping(target = "formula", source = "formula", qualifiedByName = "decodeFormula")
    SegmentHierarchyDTO toHierarchyDTO(Segment segment);

    @Mapping(target = "formula", source = "formula", qualifiedByName = "encodeFormula")
    void updateEntityFromDTO(@MappingTarget Segment segment, SegmentUpdateDTO segmentUpdateDTO);
}
