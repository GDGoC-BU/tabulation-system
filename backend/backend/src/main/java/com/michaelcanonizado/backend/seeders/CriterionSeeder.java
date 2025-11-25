package com.michaelcanonizado.backend.seeders;

import com.michaelcanonizado.backend.models.Criterion;
import com.michaelcanonizado.backend.models.Phase;
import com.michaelcanonizado.backend.models.Segment;
import com.michaelcanonizado.backend.repositories.CriterionRepository;
import com.michaelcanonizado.backend.repositories.PhaseRepository;
import com.michaelcanonizado.backend.utilities.PhaseSegmentCriteriaData;
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
