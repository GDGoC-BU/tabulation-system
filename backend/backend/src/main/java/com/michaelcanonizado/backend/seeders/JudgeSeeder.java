package com.michaelcanonizado.backend.seeders;

import com.michaelcanonizado.backend.models.Judge;
import com.michaelcanonizado.backend.models.Pageant;
import com.michaelcanonizado.backend.repositories.JudgeRepository;
import com.michaelcanonizado.backend.repositories.PageantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Order(4)
public class JudgeSeeder implements CommandLineRunner {
    private final JudgeRepository judgeRepository;
    private final PageantRepository pageantRepository;

    @Autowired
    public JudgeSeeder(JudgeRepository judgeRepository, PageantRepository pageantRepository) {
        this.judgeRepository = judgeRepository;
        this.pageantRepository = pageantRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Pageant pageant = pageantRepository.findAll().getFirst();

        List<Judge> judges = Arrays.asList(
                new Judge("judge1","1*************", pageant),
                new Judge("judge2","2*************", pageant),
                new Judge("judge3","3*************", pageant),
                new Judge("judge4","4*************", pageant)
        );

        judges.forEach(judgeRepository::save);
    }
}
