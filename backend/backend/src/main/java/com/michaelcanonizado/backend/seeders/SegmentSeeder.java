package com.michaelcanonizado.backend.seeders;

import com.michaelcanonizado.backend.models.Phase;
import com.michaelcanonizado.backend.models.Segment;
import com.michaelcanonizado.backend.repositories.PhaseRepository;
import com.michaelcanonizado.backend.repositories.SegmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class SegmentSeeder implements DatabaseSeeder {
    private final SegmentRepository segmentRepository;
    private final PhaseRepository phaseRepository;

    @Autowired
    public SegmentSeeder(SegmentRepository segmentRepository, PhaseRepository phaseRepository) {
        this.segmentRepository = segmentRepository;
        this.phaseRepository = phaseRepository;
    }

    @Override
    public void seed() {
        List<Phase> phases = phaseRepository.findAll();

        List<Segment> segments = Arrays.asList(
                new Segment("Swimwear", 1, null, null, phases.get(0)),
                new Segment("Formal Attire", 2, null, null, phases.get(0)),
                new Segment("Preliminary Question and Answer", 3, null, null, phases.get(0)),
                new Segment("Final Question and Answer", 4, 4, "CHANGE THIS FORMULA!", phases.get(0))
        );

        segments.forEach(segmentRepository::save);
    }
}
