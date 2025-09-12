package com.michaelcanonizado.backend.seeders;

import com.michaelcanonizado.backend.models.Manager;
import com.michaelcanonizado.backend.models.ManagerRole;
import com.michaelcanonizado.backend.repositories.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ManagerSeeder implements DatabaseSeeder {
    private final ManagerRepository repository;

    @Autowired
    public ManagerSeeder(ManagerRepository repository) {
        this.repository = repository;
    }

    @Override
    public void seed() {
        repository.save(new Manager("admin", "a*****", ManagerRole.ADMIN));
    }
}
