package com.gdgocbu.tabulation.backend.seeders;

import com.gdgocbu.tabulation.backend.models.Pageant;
import com.gdgocbu.tabulation.backend.repositories.PageantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class PageantSeeder implements DatabaseSeeder {
    @Autowired
    private PageantRepository repository;

    @Override
    public void seed() {
        repository.save(new Pageant("Mr. and Ms. Bicol University 2025"));
    }
}
