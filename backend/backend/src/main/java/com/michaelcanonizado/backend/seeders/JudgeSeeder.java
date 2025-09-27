package com.michaelcanonizado.backend.seeders;

import com.michaelcanonizado.backend.models.Judge;
import com.michaelcanonizado.backend.models.Pageant;
import com.michaelcanonizado.backend.repositories.JudgeRepository;
import com.michaelcanonizado.backend.repositories.PageantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class JudgeSeeder implements DatabaseSeeder {
    @Autowired
    private JudgeRepository judgeRepository;

    @Autowired
    private PageantRepository pageantRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void seed() {
        Pageant pageant = pageantRepository.findAll().getFirst();

        List<Judge> judges = Arrays.asList(
                new Judge("judge 1","1234", pageant),
                new Judge("judge 2","1234", pageant),
                new Judge("judge 3","1234", pageant),
                new Judge("judge 4","1234", pageant)
        );

        judges.forEach(judge -> {
            String password  = judge.getPasswordHash();
            String passwordHash = passwordEncoder.encode(password);
            judge.setPasswordHash(passwordHash);
            judgeRepository.save(judge);
        });
    }
}
