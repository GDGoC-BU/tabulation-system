package com.michaelcanonizado.backend.seeders;

import com.michaelcanonizado.backend.models.College;
import com.michaelcanonizado.backend.repositories.CollegeRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Order(1)
public class CollegeSeeder implements CommandLineRunner {
    private final CollegeRepository repository;

    private final List<College> colleges = Arrays.asList(
            new College("BUCAF", "Bicol University College of AF"),
            new College("BUCAL", "Bicol University College of Arts and Letters"),
            new College("BUCBEM", "Bicol University College of Business, Economics, and Management"),
            new College("BUCE", "Bicol University College of Education"),
            new College("BUCENG", "Bicol University College of Engineering"),
            new College("BUCIT", "Bicol University College of Industrial Technology"),
            new College("BUCN", "Bicol University College of Nursing"),
            new College("BUCS", "Bicol University College of Science"),
            new College("BUCSSP", "Bicol University College of Social Sciences and Philosophy"),
            new College("BUGC", "Bicol University Guinobatan/Gubat Campuss"),
            new College("BUIDeA", "Bicol University College of Architecture"),
            new College("BUIPESR", "Bicol University IPSER"),
            new College("BUJMRIGD", "Bicol University JMRIGD"),
            new College("BUPC", "Bicol University Polangui Campus"),
            new College("BUTC", "Bicol University Tobaco Campus")
    );

    @Autowired
    public CollegeSeeder(CollegeRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        colleges.forEach(repository::save);
    }
}
