package com.michaelcanonizado.backend.services;

import com.michaelcanonizado.backend.dtos.college.CollegeCreateDTO;
import com.michaelcanonizado.backend.dtos.college.CollegeDetailedDTO;
import com.michaelcanonizado.backend.dtos.college.CollegeSummaryDTO;
import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.entity.EntityMismatchException;
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

    public CollegeDetailedDTO getCollege(UUID id) {
        College college = repository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("College not found!", ErrorCode.COLLEGE_NOT_FOUND);
        });
        return mapper.toDetailedDTO(college);
    }

    public CollegeDetailedDTO addCollege(CollegeCreateDTO collegeCreateDTO) {
        College college = repository.save(mapper.toEntity(collegeCreateDTO));
        return mapper.toDetailedDTO(college);
    }

    public CollegeSummaryDTO updateCollege(UUID id, CollegeSummaryDTO collegeSummaryDTO) {
        if (!id.equals(collegeSummaryDTO.id())) {
            throw new EntityMismatchException(
                    "Path id " + id + " and College.id " + collegeSummaryDTO.id() + " doesn't match.",
                    ErrorCode.COLLEGE_MISMATCH
            );
        }

        College college = repository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("College of id " + id + " doesn't exist.", ErrorCode.COLLEGE_NOT_FOUND);
        });
        mapper.updateEntityFromDTO(college, collegeSummaryDTO);
        return mapper.toSummaryDTO(repository.save(college));
    }

    public College findById(UUID id) {
        /* Add exception to thrown when college is not present */
        return repository.findById(id).orElseThrow(()-> new EntityNotFoundException("College of id " + id + " not found!", ErrorCode.COLLEGE_NOT_FOUND));
    }
}
