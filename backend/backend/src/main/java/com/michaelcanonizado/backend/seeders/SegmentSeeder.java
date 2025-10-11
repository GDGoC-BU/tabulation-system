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
                new Segment("Interview Proper", 1, null, null, phases.get(0)),
                new Segment("Swimwear", 1, null, null, phases.get(1)),
                new Segment("Formal Attire", 2, null, null, phases.get(1)),
                new Segment("Question and Answer", 3, null, null, phases.get(1)),
                new Segment("Final Round", 4, null, null, phases.get(1))
        );

        segments.forEach(segmentRepository::save);
    }
}
