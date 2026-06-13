package com.gdgocbu.tabulation.backend.controllers;

import com.gdgocbu.tabulation.backend.annotations.RequirePageantStatus;
import com.gdgocbu.tabulation.backend.dtos.phase.PhaseCreateDTO;
import com.gdgocbu.tabulation.backend.dtos.phase.PhaseDetailedDTO;
import com.gdgocbu.tabulation.backend.dtos.phase.PhaseSummaryDTO;
import com.gdgocbu.tabulation.backend.dtos.phase.PhaseUpdateDTO;
import com.gdgocbu.tabulation.backend.dtos.segment.SegmentDetailedDTO;
import com.gdgocbu.tabulation.backend.models.PageantStatus;
import com.gdgocbu.tabulation.backend.services.PhaseService;
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
public class PhaseController {
    @Autowired
    private PhaseService service;

    @PostMapping("/phases")
    public ResponseEntity<PhaseDetailedDTO> addPhase(@RequestBody @Valid PhaseCreateDTO phaseCreateDTO) {
        PhaseDetailedDTO phase = service.addPhase(phaseCreateDTO);
        return new ResponseEntity<>(phase, HttpStatus.CREATED);
    }

    @PostMapping("/phases/{id}/start")
    public ResponseEntity<PhaseDetailedDTO> startPhase(@PathVariable UUID id) {
        PhaseDetailedDTO phase = service.startPhase(id);
        return new ResponseEntity<>(phase, HttpStatus.OK);
    }

    @PostMapping("/phases/{id}/close")
    public ResponseEntity<PhaseDetailedDTO> closePhase(@PathVariable UUID id) {
        PhaseDetailedDTO phase = service.closePhase(id);
        return new ResponseEntity<>(phase, HttpStatus.OK);
    }

    @GetMapping("/phases/{id}")
    public ResponseEntity<PhaseDetailedDTO> getPhase(@PathVariable UUID id) {
        PhaseDetailedDTO phase = service.getPhase(id);
        return new ResponseEntity<>(phase, HttpStatus.OK);
    }

    @GetMapping("/phases/ongoing")
    public ResponseEntity<PhaseDetailedDTO> getOngoingPhase() {
        PhaseDetailedDTO phase = service.getOngoingPhase();
        if (phase == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(phase, HttpStatus.OK);
    }

    @GetMapping("/phases")
    public ResponseEntity<List<PhaseSummaryDTO>> getPhases() {
        List<PhaseSummaryDTO> phases = service.getPhases();
        return new ResponseEntity<>(phases, HttpStatus.OK);
    }

    @PutMapping("/phases/{id}")
    public ResponseEntity<PhaseDetailedDTO> updatePhases(@PathVariable UUID id, @RequestBody @Valid PhaseUpdateDTO phaseUpdateDTO) {
        PhaseDetailedDTO phase = service.updatePhase(id, phaseUpdateDTO);
        return new ResponseEntity<>(phase, HttpStatus.OK);
    }

    @DeleteMapping("/phases/{id}")
    public ResponseEntity<Void> deletePhase(@PathVariable UUID id) {
        service.deletePhase(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
