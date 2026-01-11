package com.michaelcanonizado.backend.seeders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.michaelcanonizado.backend.models.Formula;
import com.michaelcanonizado.backend.models.Phase;
import com.michaelcanonizado.backend.models.Segment;
import com.michaelcanonizado.backend.repositories.PhaseRepository;
import com.michaelcanonizado.backend.repositories.SegmentRepository;
import com.michaelcanonizado.backend.utilities.PhaseSegmentCriteriaData;
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
                segments.add(
                        new Segment(
                                segmentTemp.getName(),
                                segmentTemp.getSequence(),
                                phase
                        )
                );
            });
        });
        segmentRepository.saveAll(segments);
    }
}
