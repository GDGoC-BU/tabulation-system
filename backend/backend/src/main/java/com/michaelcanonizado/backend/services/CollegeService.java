package com.michaelcanonizado.backend.services;

import com.michaelcanonizado.backend.annotations.RequirePageantStatus;
import com.michaelcanonizado.backend.dtos.college.CollegeCreateDTO;
import com.michaelcanonizado.backend.dtos.college.CollegeDetailedDTO;
import com.michaelcanonizado.backend.dtos.college.CollegeSummaryDTO;
import com.michaelcanonizado.backend.dtos.college.CollegeUpdateDTO;
import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.customs.EntityNotFoundException;
import com.michaelcanonizado.backend.mappers.CollegeMapper;
import com.michaelcanonizado.backend.models.College;
import com.michaelcanonizado.backend.models.PageantStatus;
import com.michaelcanonizado.backend.repositories.CollegeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class CollegeService {
    @Autowired
    private CollegeRepository repository;

    @Autowired
    private CollegeMapper mapper;

    @Transactional
    public CollegeDetailedDTO addCollege(CollegeCreateDTO collegeCreateDTO) {
        College college = repository.save(mapper.toEntity(collegeCreateDTO));
        return mapper.toDetailedDTO(college);
    }

    @Transactional
    public CollegeDetailedDTO getCollege(UUID id) {
        College college = repository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("College not found!", ErrorCode.ENTITY_NOT_FOUND);
        });
        return mapper.toDetailedDTO(college);
    }

    public List<CollegeSummaryDTO> getColleges() {
        return repository.findAll().stream().map(college -> {
            return mapper.toSummaryDTO(college);
        }).toList();
    }

    public CollegeSummaryDTO updateCollege(UUID id, CollegeUpdateDTO collegeUpdateDTO) {
        College college = repository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Can't update! College not found.", ErrorCode.ENTITY_NOT_FOUND);
        });
        mapper.updateEntityFromDTO(college, collegeUpdateDTO);
        return mapper.toSummaryDTO(repository.save(college));
    }

    public void deleteCollege(UUID id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Can't delete! College not found.", ErrorCode.ENTITY_NOT_FOUND);
        }

        repository.deleteById(id);
    }

    public College findById(UUID id) {
        return repository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("College not found!", ErrorCode.ENTITY_NOT_FOUND);
        });
    }
}
