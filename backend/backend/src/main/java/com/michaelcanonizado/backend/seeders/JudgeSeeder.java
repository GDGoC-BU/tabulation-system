package com.michaelcanonizado.backend.seeders;

import com.michaelcanonizado.backend.models.Honorific;
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
                new Judge("JUDGE_1","Comoda_2025", "Mickha Ella","Comoda", Honorific.MS, 1, pageant),
                new Judge("JUDGE_2","Custodio_2025", "Marae Alaine","Custodio", Honorific.MS, 2, pageant),
                new Judge("JUDGE_3","Llanto_2025", "Neal","Llanto", Honorific.DR, 3, pageant),
                new Judge("JUDGE_4","Santilla_2025", "Apple","Santilla", Honorific.MX, 4, pageant),
                new Judge("JUDGE_5","Orosco_2025", "Aujel","Orosco", Honorific.MR, 5, pageant)
        );

        judges.forEach(judge -> {
            /* Encode the passwords */
            String password  = judge.getPasswordHash();
            String passwordHash = passwordEncoder.encode(password);
            judge.setPasswordHash(passwordHash);

            judgeRepository.save(judge);
        });
    }
}
