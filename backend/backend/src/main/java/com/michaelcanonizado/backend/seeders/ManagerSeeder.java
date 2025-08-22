package com.michaelcanonizado.backend.seeders;

import com.michaelcanonizado.backend.models.Manager;
import com.michaelcanonizado.backend.models.ManagerRole;
import com.michaelcanonizado.backend.repositories.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(8)
public class ManagerSeeder implements CommandLineRunner {
    private final ManagerRepository repository;

    @Autowired
    public ManagerSeeder(ManagerRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        repository.save(new Manager("admin", "a*****", ManagerRole.ADMIN));
    }
}
