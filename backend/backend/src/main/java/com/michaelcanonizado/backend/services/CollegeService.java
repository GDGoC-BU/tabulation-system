package com.michaelcanonizado.backend.services;

import com.michaelcanonizado.backend.dtos.college.CollegeSummaryDTO;
import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.entity.EntityNotFoundException;
import com.michaelcanonizado.backend.mapper.CollegeMapper;
import com.michaelcanonizado.backend.models.College;
import com.michaelcanonizado.backend.repositories.CollegeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CollegeService {
    @Autowired
    private CollegeRepository repository;
    @Autowired
    private CollegeMapper mapper;

    public List<CollegeSummaryDTO> getColleges() {
        return repository.findAll().stream().map(college -> {
            return mapper.toSummaryDTO(college);
        }).toList();
    }

    public College findById(UUID id) {
        /* Add exception to thrown when college is not present */
        return repository.findById(id).orElseThrow(()-> new EntityNotFoundException("College of id " + id + " not found!", ErrorCode.COLLEGE_NOT_FOUND));
    }
}
