package com.michaelcanonizado.backend.controllers;

import com.michaelcanonizado.backend.dtos.criterion.CriterionCreateDTO;
import com.michaelcanonizado.backend.dtos.criterion.CriterionSummaryDTO;
import com.michaelcanonizado.backend.services.CriterionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/api/v1")
public class CriterionController {
    @Autowired
    private CriterionService service;

    @PostMapping("/criteria")
    public ResponseEntity<CriterionSummaryDTO> addCriterion(@RequestBody CriterionCreateDTO criterionCreateDTO) {
        CriterionSummaryDTO criterion = service.addCriterion(criterionCreateDTO);
        return new ResponseEntity<>(criterion, HttpStatus.CREATED);
    }

    @PutMapping("/criteria/{id}")
    public ResponseEntity<CriterionSummaryDTO> updateCriterion(@PathVariable UUID id, @RequestBody CriterionSummaryDTO criterionSummaryDTO) {
        CriterionSummaryDTO criterion = service.updateCriterion(id, criterionSummaryDTO);
        return new ResponseEntity<>(criterion, HttpStatus.OK);
    }
}
