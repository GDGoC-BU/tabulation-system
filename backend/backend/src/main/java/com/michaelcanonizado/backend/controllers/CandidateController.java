package com.michaelcanonizado.backend.controllers;

import com.michaelcanonizado.backend.dtos.candidate.CandidateCreateDTO;
import com.michaelcanonizado.backend.services.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/v1")
public class CandidateController {
    @Autowired
    private CandidateService service;

    @PostMapping("/candidates")
    public ResponseEntity<Void> addCandidate(@RequestBody CandidateCreateDTO candidateCreateDTO) {
        service.addCandidate(candidateCreateDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
