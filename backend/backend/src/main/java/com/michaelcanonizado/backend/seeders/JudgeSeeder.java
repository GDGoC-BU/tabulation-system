package com.michaelcanonizado.backend.seeders;

import com.michaelcanonizado.backend.models.Judge;
import com.michaelcanonizado.backend.repositories.JudgeRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Order(4)
public class JudgeSeeder implements CommandLineRunner {
    private final JudgeRepository repository;

    private final List<Judge> judges = Arrays.asList(
            new Judge("judge1","1*************"),
            new Judge("judge2","2*************"),
            new Judge("judge3","3*************"),
            new Judge("judge4","4*************")
    );

    @Autowired
    public JudgeSeeder(JudgeRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        judges.forEach(repository::save);
    }
}
