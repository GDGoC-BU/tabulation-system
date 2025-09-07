package com.michaelcanonizado.backend.seeders;

import com.michaelcanonizado.backend.models.Pageant;
import com.michaelcanonizado.backend.repositories.PageantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Order(0)
public class PageantSeeder implements CommandLineRunner {
    private final PageantRepository repository;

    @Autowired
    public PageantSeeder(PageantRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        repository.save(new Pageant("MMBU 2025-2026"));
    }
}
