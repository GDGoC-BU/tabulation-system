package com.michaelcanonizado.backend.services;

import com.michaelcanonizado.backend.models.College;
import com.michaelcanonizado.backend.repositories.CollegeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollegeService {
    @Autowired
    private CollegeRepository repository;

    public List<College> getColleges() {
        return repository.findAll();
    }
}
