package com.gdgocbu.tabulation.backend.seeders;

import com.gdgocbu.tabulation.backend.models.Pageant;
import com.gdgocbu.tabulation.backend.models.Phase;
import com.gdgocbu.tabulation.backend.repositories.PageantRepository;
import com.gdgocbu.tabulation.backend.repositories.PhaseRepository;
import com.gdgocbu.tabulation.backend.utilities.PhaseSegmentCriteriaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PhaseSeeder implements DatabaseSeeder {
    @Autowired
    private PhaseRepository phaseRepository;

    @Autowired
    private PageantRepository pageantRepository;


    @Override
    public void seed() {
        Pageant pageant = pageantRepository.findAll().getFirst();

        List<Phase> phases = new ArrayList<>();
        PhaseSegmentCriteriaData.phases.forEach(phaseTemp -> {
            phases.add(new Phase(phaseTemp.getName(), phaseTemp.getSequence(), pageant));
        });
        phaseRepository.saveAll(phases);
    }
}
