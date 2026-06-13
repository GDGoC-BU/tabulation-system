package com.gdgocbu.tabulation.backend.models;

import com.gdgocbu.tabulation.backend.dtos.criterion.CriterionBreakdownDTO;
import com.gdgocbu.tabulation.backend.dtos.phase.PhaseBreakdownDTO;
import com.gdgocbu.tabulation.backend.dtos.score.ScoreBreakdownDTO;
import com.gdgocbu.tabulation.backend.dtos.segment.SegmentBreakdownDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CriteriaBreakdown {
    private PhaseBreakdownDTO phase;
    private SegmentBreakdownDTO segment;
    private CriterionBreakdownDTO criterion;
    private BigDecimal averageScore;
    private List<ScoreBreakdownDTO> scores;
}
