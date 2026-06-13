package com.gdgocbu.tabulation.backend.controllers;

import com.gdgocbu.tabulation.backend.dtos.candidate.CandidateCreateDTO;
import com.gdgocbu.tabulation.backend.dtos.candidate.CandidateSummaryDTO;
import com.gdgocbu.tabulation.backend.dtos.candidate.CandidateUpdateDTO;
import com.gdgocbu.tabulation.backend.services.CandidateService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("api/v1")
public class CandidateController {
    @Autowired
    private CandidateService service;

    @PostMapping("/candidates")
    public ResponseEntity<CandidateSummaryDTO> addCandidate(@RequestBody @Valid CandidateCreateDTO candidateCreateDTO) {
        CandidateSummaryDTO candidate = service.addCandidate(candidateCreateDTO);
        return new ResponseEntity<>(candidate, HttpStatus.CREATED);
    }

    @GetMapping("/candidates/{id}")
    public ResponseEntity<CandidateSummaryDTO> getCandidate(@PathVariable UUID id) {
        CandidateSummaryDTO candidate = service.getCandidate(id);
        return new ResponseEntity<>(candidate, HttpStatus.OK);
    }

    @GetMapping("/candidates")
    public ResponseEntity<List<CandidateSummaryDTO>> getCandidates() {
        List<CandidateSummaryDTO> candidates = service.getCandidates();
        return new ResponseEntity<>(candidates, HttpStatus.OK);
    }

    @PutMapping("/candidates/{id}")
    public ResponseEntity<CandidateSummaryDTO> updateCandidate(@PathVariable UUID id, @RequestBody @Valid CandidateUpdateDTO candidateUpdateDTO) {
        CandidateSummaryDTO candidate = service.updateCandidate(id, candidateUpdateDTO);
        return new ResponseEntity<>(candidate, HttpStatus.OK);
    }

    @DeleteMapping("/candidates/{id}")
    public  ResponseEntity<Void> deleteCandidate(@PathVariable UUID id) {
        service.deleteCandidate(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
