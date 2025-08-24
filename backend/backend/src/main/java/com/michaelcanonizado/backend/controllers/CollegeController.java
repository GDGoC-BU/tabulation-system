package com.michaelcanonizado.backend.controllers;

import com.michaelcanonizado.backend.dtos.college.CollegeCreateDTO;
import com.michaelcanonizado.backend.dtos.college.CollegeDetailedDTO;
import com.michaelcanonizado.backend.dtos.college.CollegeSummaryDTO;
import com.michaelcanonizado.backend.services.CollegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api/v1/")
public class CollegeController {
    @Autowired
    private CollegeService service;

    @GetMapping("/colleges")
    public ResponseEntity<List<CollegeSummaryDTO>> getColleges() {
        List<CollegeSummaryDTO> colleges = service.getColleges();
        return new ResponseEntity<>(colleges, HttpStatus.OK);
    }

    @PostMapping("/colleges")
    public ResponseEntity<CollegeDetailedDTO> addCollege(@RequestBody CollegeCreateDTO collegeCreateDTO) {
        CollegeDetailedDTO college = service.addCollege(collegeCreateDTO);
        return new ResponseEntity<>(college, HttpStatus.CREATED);
    }

    @PutMapping("/colleges/{id}")
    public ResponseEntity<CollegeSummaryDTO> updateCollege(@PathVariable UUID id, @RequestBody CollegeSummaryDTO collegeSummaryDTO) {
        CollegeSummaryDTO college = service.updateCollege(id, collegeSummaryDTO);
        return new ResponseEntity<>(college, HttpStatus.OK);
    }
}
