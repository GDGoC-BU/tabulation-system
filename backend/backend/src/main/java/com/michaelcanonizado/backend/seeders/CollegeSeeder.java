package com.michaelcanonizado.backend.seeders;

import com.michaelcanonizado.backend.models.College;
import com.michaelcanonizado.backend.repositories.CollegeRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Getter
class CollegeItem {
    String code;
    String name;

    CollegeItem(String code, String name) {
        this.code = code;
        this.name = name;
    }
}

@Component
@Order(1)
public class CollegeSeeder implements CommandLineRunner {
    private final CollegeRepository repository;

    private final List<CollegeItem> colleges = Arrays.asList(
            new CollegeItem("BUCAF", "Bicol University College of AF"),
            new CollegeItem("BUCAL", "Bicol University College of Arts and Letters"),
            new CollegeItem("BUCBEM", "Bicol University College of Business, Economics, and Management"),
            new CollegeItem("BUCE", "Bicol University College of Education"),
            new CollegeItem("BUCENG", "Bicol University College of Engineering"),
            new CollegeItem("BUCIT", "Bicol University College of Industrial Technology"),
            new CollegeItem("BUCN", "Bicol University College of Nursing"),
            new CollegeItem("BUCS", "Bicol University College of Science"),
            new CollegeItem("BUCSSP", "Bicol University College of Social Sciences and Philosophy"),
            new CollegeItem("BUGC", "Bicol University Guinobatan/Gubat Campuss"),
            new CollegeItem("BUIDeA", "Bicol University College of Architecture"),
            new CollegeItem("BUIPESR", "Bicol University IPSER"),
            new CollegeItem("BUJMRIGD", "Bicol University JMRIGD"),
            new CollegeItem("BUPC", "Bicol University Polangui Campus"),
            new CollegeItem("BUTC", "Bicol University Tobaco Campus")
    );

    @Autowired
    public CollegeSeeder(CollegeRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        colleges.forEach(collegeItem -> {
            repository.save(new College(collegeItem.getCode(), collegeItem.getName()));
        });
    }
}
