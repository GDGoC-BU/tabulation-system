package com.gdgocbu.tabulation.backend.seeders;

import com.gdgocbu.tabulation.backend.models.Admin;
import com.gdgocbu.tabulation.backend.repositories.AdminRepository;
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
        String username = "gdgoc_admin";
        String password = "4321";
        String passwordHash = passwordEncoder.encode(password);
        repository.save(new Admin(username, passwordHash));
    }
}
