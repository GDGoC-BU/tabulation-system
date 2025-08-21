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

@Getter
@AllArgsConstructor
class JudgeItem {
    private String username;
    private String passwordHash;
}

@Component
@Order(4)
public class JudgeSeeder implements CommandLineRunner {
    private final JudgeRepository repository;

    private final List<JudgeItem> judges = Arrays.asList(
            new JudgeItem("judge1","1*************"),
            new JudgeItem("judge2","2*************"),
            new JudgeItem("judge3","3*************"),
            new JudgeItem("judge4","4*************")
    );

    @Autowired
    public JudgeSeeder(JudgeRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        judges.forEach(judgeItem -> {
            repository.save(new Judge(judgeItem.getUsername(), judgeItem.getPasswordHash()));
        });
    }
}
