package com.michaelcanonizado.backend.seeders;

import com.michaelcanonizado.backend.models.Judge;
import com.michaelcanonizado.backend.models.Pageant;
import com.michaelcanonizado.backend.repositories.JudgeRepository;
import com.michaelcanonizado.backend.repositories.PageantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class JudgeSeeder implements DatabaseSeeder {
    private final JudgeRepository judgeRepository;
    private final PageantRepository pageantRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Autowired
    public JudgeSeeder(JudgeRepository judgeRepository, PageantRepository pageantRepository) {
        this.judgeRepository = judgeRepository;
        this.pageantRepository = pageantRepository;
    }

    @Override
    public void seed() {
        Pageant pageant = pageantRepository.findAll().getFirst();

        List<Judge> judges = Arrays.asList(
                new Judge("judge1","1234", pageant),
                new Judge("judge2","1234", pageant),
                new Judge("judge3","1234", pageant),
                new Judge("judge4","1234", pageant)
        );

        judges.forEach(judge -> {
            String password  = judge.getPasswordHash();
            String passwordHash = encoder.encode(password);
            judge.setPasswordHash(passwordHash);
            judgeRepository.save(judge);
        });
    }
}
