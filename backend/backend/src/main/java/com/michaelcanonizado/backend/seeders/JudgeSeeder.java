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
                new Judge("a","", "James Edward","Tambobong", Honorific.MR, 1, pageant),
                new Judge("b","", "Rhondon","Ricafort", Honorific.MR, 2, pageant),
                new Judge("c","", "Neal","Llanto", Honorific.DR, 3, pageant),
                new Judge("d","", "John Paul","Regaspi", Honorific.MX, 4, pageant),
                new Judge("e","", "Phoebe Kate","Calisin", Honorific.MS, 5, pageant),

                /* Coronation Night Judges */
                new Judge("f","", "Patrixia Sherly","Santos", Honorific.ATTY, 6, pageant),
                new Judge("g","", "Richard","Milante", Honorific.MR, 7, pageant),
                new Judge("h","", "Abelardo Billy","Balin", Honorific.MR, 8, pageant),
                new Judge("i","", "Juan","Sarte", Honorific.MR, 9, pageant),
                new Judge("j","", "Jorim","Sumangid", Honorific.MX, 10, pageant),
                new Judge("k","", "Alexandra Krishna","Orino", Honorific.MS, 11, pageant),
                new Judge("l","", "Gabriel","Aguirre", Honorific.MR, 12, pageant),
                new Judge("m","", "Margarette Siat","Briton", Honorific.MS, 13, pageant),
                new Judge("n","", "Eileen Kae","Relao", Honorific.MS, 14, pageant)
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
