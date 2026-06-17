package com.gdgocbu.tabulation.backend.seeders;

import com.gdgocbu.tabulation.backend.models.Criterion;
import com.gdgocbu.tabulation.backend.models.Phase;
import com.gdgocbu.tabulation.backend.models.Segment;
import com.gdgocbu.tabulation.backend.repositories.CriterionRepository;
import com.gdgocbu.tabulation.backend.repositories.PhaseRepository;
import com.gdgocbu.tabulation.backend.utilities.PhaseSegmentCriteriaData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Component
public class CriterionSeeder implements DatabaseSeeder {
    @Autowired
    private PhaseRepository phaseRepository;

    @Autowired
    private CriterionRepository criterionRepository;

    @Transactional
    @Override
    public void seed() {
        List<Phase> phases = phaseRepository.findAll();
        List<Criterion> criteria = new ArrayList<>();

        phases.forEach(phase -> {
            List<Segment> segments = phase.getSegments();
            segments.forEach(segment -> {
                PhaseSegmentCriteriaData.SegmentTemp segmentTemp =
                        PhaseSegmentCriteriaData.segmentByPhaseNameAndSegmentName
                                .get(phase.getName())
                                .get(segment.getName());

                if (segmentTemp == null) {
                    throw new RuntimeException(
                            "Error seeding criteria! no static criteria list found for " +
                            phase.getName() +
                            "." +
                            segment.getName()
                    );
                }

                segmentTemp.getCriteria().forEach(criterionTemp -> {
                    criteria.add(new Criterion(criterionTemp.getName(), criterionTemp.getMaxScore(), segment));
                });
            });
        });
        criterionRepository.saveAll(criteria);
    }
}
