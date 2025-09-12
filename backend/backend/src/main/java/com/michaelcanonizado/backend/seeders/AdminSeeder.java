package com.michaelcanonizado.backend.seeders;

import com.michaelcanonizado.backend.models.Admin;
import com.michaelcanonizado.backend.repositories.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdminSeeder implements DatabaseSeeder {
    private final AdminRepository repository;

    @Autowired
    public AdminSeeder(AdminRepository repository) {
        this.repository = repository;
    }

    @Override
    public void seed() {
        repository.save(new Admin("admin", "a*****"));
    }
}
