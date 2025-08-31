package com.michaelcanonizado.backend.controllers;

import com.michaelcanonizado.backend.dtos.pageant.PageantCreateDTO;
import com.michaelcanonizado.backend.dtos.pageant.PageantSummaryDTO;
import com.michaelcanonizado.backend.dtos.pageant.PageantUpdateDTO;
import com.michaelcanonizado.backend.services.PageantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1")
public class PageantController {
    @Autowired
    private PageantService service;

    @PostMapping("/pageant")
    public ResponseEntity<PageantSummaryDTO> addPageant(@RequestBody @Valid PageantCreateDTO pageantCreateDTO) {
        PageantSummaryDTO pageant = service.addPageant(pageantCreateDTO);
        return new ResponseEntity<>(pageant, HttpStatus.CREATED);
    }

    @PostMapping("/pageant/start")
    public ResponseEntity<PageantSummaryDTO> startPageant() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/pageant/finalize")
    public ResponseEntity<PageantSummaryDTO> finalizePageant() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/pageant/close")
    public ResponseEntity<PageantSummaryDTO> closePageant() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/pageant")
    public ResponseEntity<PageantSummaryDTO> getPageant() {
        PageantSummaryDTO pageant = service.getPageant();
        return new ResponseEntity<>(pageant, HttpStatus.OK);
    }

    @PutMapping("/pageant")
    public ResponseEntity<PageantSummaryDTO> updatePageant(@RequestBody @Valid PageantUpdateDTO pageantUpdateDTO) {
        PageantSummaryDTO pageant = service.updatePageant(pageantUpdateDTO);
        return new ResponseEntity<>(pageant, HttpStatus.OK);
    }
}
