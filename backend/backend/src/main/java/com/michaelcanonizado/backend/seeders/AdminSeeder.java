package com.michaelcanonizado.backend.seeders;

import com.michaelcanonizado.backend.models.Admin;
import com.michaelcanonizado.backend.repositories.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminSeeder implements DatabaseSeeder {
    @Autowired
    private AdminRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void seed() {
        String username = "admin";
        String password = "1234";
        String passwordHash = passwordEncoder.encode(password);
        repository.save(new Admin(username, passwordHash));
    }
}
