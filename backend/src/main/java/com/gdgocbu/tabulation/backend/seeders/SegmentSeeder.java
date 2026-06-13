package com.gdgocbu.tabulation.backend.seeders;

import com.gdgocbu.tabulation.backend.models.*;
import com.gdgocbu.tabulation.backend.repositories.*;
import com.gdgocbu.tabulation.backend.utilities.PhaseSegmentCriteriaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SegmentSeeder implements DatabaseSeeder {
    @Autowired
    private SegmentRepository segmentRepository;

    @Autowired
    private PhaseRepository phaseRepository;

    @Override
    public void seed() {
        List<Phase> phases = phaseRepository.findAll();
        List<Segment> segments = new ArrayList<>();

        /* Loop through each phase in the database */
        phases.forEach(phase -> {
            /* Get the static phase using name */
            PhaseSegmentCriteriaData.PhaseTemp phaseTemp = PhaseSegmentCriteriaData.phaseByName.get(phase.getName());

            if (phaseTemp == null) {
                throw new RuntimeException(
                        "Error seeding segments! no static list found "
                );
            }

            /* Go through the ist of segments of the static phase and store it in the database */
            phaseTemp.getSegments().forEach(segmentTemp -> {
                Segment segment = new Segment(
                        segmentTemp.getName(),
                        segmentTemp.getSequence(),
                        phase
                );
                segments.add(segment);
            });
        });
        segmentRepository.saveAll(segments);
    }
}
