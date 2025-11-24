package com.michaelcanonizado.backend.models;

import com.michaelcanonizado.backend.dtos.criterion.CriterionBreakdownDTO;
import com.michaelcanonizado.backend.dtos.phase.PhaseBreakdownDTO;
import com.michaelcanonizado.backend.dtos.score.ScoreBreakdownDTO;
import com.michaelcanonizado.backend.dtos.segment.SegmentBreakdownDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CriteriaBreakdown {
    private PhaseBreakdownDTO phase;
    private SegmentBreakdownDTO segment;
    private CriterionBreakdownDTO criterion;
    private Double averageScore;
    private List<ScoreBreakdownDTO> scores;
}
