package com.michaelcanonizado.backend.seeders;

import com.michaelcanonizado.backend.models.Honorific;
import com.michaelcanonizado.backend.models.Judge;
import com.michaelcanonizado.backend.models.Pageant;
import com.michaelcanonizado.backend.repositories.JudgeRepository;
import com.michaelcanonizado.backend.repositories.PageantRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
                /* Closed-Door Interview Judges */
                new Judge("JUDGE_1","Tambobong_2025", "James Edward","Tambobong", Honorific.MR, 1, pageant),
                new Judge("JUDGE_2","Ricafort_2025", "Rhondon","Ricafort", Honorific.MR, 2, pageant),
                new Judge("JUDGE_3","Llanto_2025", "Neal","Llanto", Honorific.DR, 3, pageant),
                new Judge("JUDGE_4","Regaspi_2025", "John Paul","Regaspi", Honorific.MX, 4, pageant),
                new Judge("JUDGE_5","Calisin_2025", "Phoebe Kate","Calisin", Honorific.MS, 5, pageant),

                /* Coronation Night Judges */
                new Judge("JUDGE_6","Santos_2025", "Patrixia Sherly","Santos", Honorific.ATTY, 6, pageant),
                new Judge("JUDGE_7","Milante_2025", "Richard","Milante", Honorific.MR, 7, pageant),
                new Judge("JUDGE_8","Balin_2025", "Abelardo Billy","Balin", Honorific.MR, 8, pageant),
                new Judge("JUDGE_9","Sarte_2025", "Juan","Sarte", Honorific.MR, 9, pageant),
                new Judge("JUDGE_10","Sumangid_2025", "Jorim","Sumangid", Honorific.MX, 10, pageant),
                new Judge("JUDGE_11","Orino_2025", "Alexandra Krishna","Orino", Honorific.MS, 11, pageant),
                new Judge("JUDGE_12","Aguirre_2025", "Gabriel","Aguirre", Honorific.MR, 12, pageant),
                new Judge("JUDGE_13","Briton_2025", "Margarette Siat","Briton", Honorific.MS, 13, pageant),
                new Judge("JUDGE_14","Relao_2025", "Eileen Kae","Relao", Honorific.MS, 14, pageant)
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
