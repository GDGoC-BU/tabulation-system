package com.michaelcanonizado.backend.controllers;

import com.michaelcanonizado.backend.dtos.phase.PhaseCreateDTO;
import com.michaelcanonizado.backend.dtos.phase.PhaseDetailedDTO;
import com.michaelcanonizado.backend.services.PhaseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/")
public class PhaseController {
    @Autowired
    private PhaseService service;

    @PostMapping("/phases")
    public ResponseEntity<PhaseDetailedDTO> addPhase(@RequestBody @Valid PhaseCreateDTO phaseCreateDTO) {
        PhaseDetailedDTO phase = service.addPhase(phaseCreateDTO);
        return new ResponseEntity<>(phase, HttpStatus.CREATED);
    }
}
