package com.michaelcanonizado.backend.seeders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.michaelcanonizado.backend.models.*;
import com.michaelcanonizado.backend.repositories.*;
import com.michaelcanonizado.backend.utilities.PhaseSegmentCriteriaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class SegmentSeeder implements DatabaseSeeder {
    @Autowired
    private PageantRepository pageantRepository;

    @Autowired
    private SegmentRepository segmentRepository;

    @Autowired
    private PhaseRepository phaseRepository;

    @Override
    public void seed() {
        UUID selectedPageantId = pageantRepository.findAll().getFirst().getId();
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

                /* Add the leaderboard */
                Leaderboard rankingLeaderboard = new Leaderboard(
                        selectedPageantId,
                        new Formula("", new ObjectMapper().createObjectNode()),
                        4
                );
                segment.setRankingLeaderboard(rankingLeaderboard);
                segments.add(segment);
            });
        });
        segmentRepository.saveAll(segments);
    }
}
