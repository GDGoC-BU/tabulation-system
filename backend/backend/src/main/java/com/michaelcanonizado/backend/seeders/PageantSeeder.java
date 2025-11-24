package com.michaelcanonizado.backend.seeders;

import com.michaelcanonizado.backend.models.Pageant;
import com.michaelcanonizado.backend.repositories.PageantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class PageantSeeder implements DatabaseSeeder {
    private final PageantRepository repository;

    @Autowired
    public PageantSeeder(PageantRepository repository) {
        this.repository = repository;
    }

    @Override
    public void seed() {
        repository.save(new Pageant("Mr. and Ms. Bicol University 2025"));
    }
}
