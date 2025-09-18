package com.michaelcanonizado.backend.seeders;

import com.michaelcanonizado.backend.models.Admin;
import com.michaelcanonizado.backend.repositories.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminSeeder implements DatabaseSeeder {
    private final AdminRepository repository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Autowired
    public AdminSeeder(AdminRepository repository) {
        this.repository = repository;
    }

    @Override
    public void seed() {
        String username = "admin";
        String password = "1234";
        String passwordHash = encoder.encode(password);
        repository.save(new Admin("admin", passwordHash));
    }
}
