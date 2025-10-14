package com.michaelcanonizado.backend.seeders;

import com.michaelcanonizado.backend.contexts.PageantContext;
import com.michaelcanonizado.backend.models.Pageant;
import com.michaelcanonizado.backend.models.Phase;
import com.michaelcanonizado.backend.repositories.PageantRepository;
import com.michaelcanonizado.backend.repositories.PhaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class PhaseSeeder implements DatabaseSeeder {
    private final PhaseRepository phaseRepository;
    private final PageantRepository pageantRepository;

    @Autowired
    public PhaseSeeder(PhaseRepository phaseRepository, PageantRepository pageantRepository) {
        this.pageantRepository = pageantRepository;
        this.phaseRepository = phaseRepository;
    }

    @Override
    public void seed() {
        Pageant pageant = pageantRepository.findAll().getFirst();

        List<Phase> phases = Arrays.asList(
                new Phase("Coronation Night", 1, pageant)
        );

        phaseRepository.saveAll(phases);
    }
}
