package com.gdgocbu.tabulation.backend.controllers;

import com.gdgocbu.tabulation.backend.dtos.criterion.CriterionCreateDTO;
import com.gdgocbu.tabulation.backend.dtos.criterion.CriterionSummaryDTO;
import com.gdgocbu.tabulation.backend.dtos.criterion.CriterionUpdateDTO;
import com.gdgocbu.tabulation.backend.services.CriterionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class CriterionController {
    @Autowired
    private CriterionService service;

    @PostMapping("/criteria")
    public ResponseEntity<CriterionSummaryDTO> addCriterion(@RequestBody @Valid CriterionCreateDTO criterionCreateDTO) {
        CriterionSummaryDTO criterion = service.addCriterion(criterionCreateDTO);
        return new ResponseEntity<>(criterion, HttpStatus.CREATED);
    }

    @GetMapping("/criteria")
    public ResponseEntity<List<CriterionSummaryDTO>> getCriteria() {
        List<CriterionSummaryDTO> criteria = service.getCriteria();
        return new ResponseEntity<>(criteria, HttpStatus.OK);
    }

    @PutMapping("/criteria/{id}")
    public ResponseEntity<CriterionSummaryDTO> updateCriterion(@PathVariable UUID id, @RequestBody @Valid CriterionUpdateDTO criterionUpdateDTO) {
        CriterionSummaryDTO criterion = service.updateCriterion(id, criterionUpdateDTO);
        return new ResponseEntity<>(criterion, HttpStatus.OK);
    }

    @DeleteMapping("/criteria/{id}")
    public ResponseEntity<Void> deleteCriterion(@PathVariable UUID id) {
        service.deleteCriterion(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
