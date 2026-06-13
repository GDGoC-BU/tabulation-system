package com.gdgocbu.tabulation.backend.controllers;

import com.gdgocbu.tabulation.backend.dtos.college.CollegeCreateDTO;
import com.gdgocbu.tabulation.backend.dtos.college.CollegeSummaryDTO;
import com.gdgocbu.tabulation.backend.dtos.college.CollegeUpdateDTO;
import com.gdgocbu.tabulation.backend.services.CollegeService;
import jakarta.validation.Valid;
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

    @PostMapping("/colleges")
    public ResponseEntity<CollegeSummaryDTO> addCollege(@RequestBody @Valid CollegeCreateDTO collegeCreateDTO) {
        CollegeSummaryDTO college = service.addCollege(collegeCreateDTO);
        return new ResponseEntity<>(college, HttpStatus.CREATED);
    }

    @GetMapping("/colleges/{id}")
    public ResponseEntity<CollegeSummaryDTO> getCollege(@PathVariable UUID id) {
        CollegeSummaryDTO college = service.getCollege(id);
        return new ResponseEntity<>(college, HttpStatus.OK);
    }

    @GetMapping("/colleges")
    public ResponseEntity<List<CollegeSummaryDTO>> getColleges() {
        List<CollegeSummaryDTO> colleges = service.getColleges();
        return new ResponseEntity<>(colleges, HttpStatus.OK);
    }

    @PutMapping("/colleges/{id}")
    public ResponseEntity<CollegeSummaryDTO> updateCollege(@PathVariable UUID id, @RequestBody @Valid CollegeUpdateDTO collegeUpdateDTO) {
        CollegeSummaryDTO college = service.updateCollege(id, collegeUpdateDTO);
        return new ResponseEntity<>(college, HttpStatus.OK);
    }

    @DeleteMapping("/colleges/{id}")
    public ResponseEntity<Void> deleteCollege(@PathVariable UUID id) {
        service.deleteCollege(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
