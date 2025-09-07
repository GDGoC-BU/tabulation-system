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

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api/v1")
public class PageantController {
    @Autowired
    private PageantService service;

    @PostMapping("/pageants")
    public ResponseEntity<PageantSummaryDTO> addPageant(@RequestBody @Valid PageantCreateDTO pageantCreateDTO) {
        PageantSummaryDTO pageant = service.addPageant(pageantCreateDTO);
        return new ResponseEntity<>(pageant, HttpStatus.CREATED);
    }

    @PostMapping("/pageants/{id}/start")
    public ResponseEntity<PageantSummaryDTO> startPageant(@PathVariable UUID id) {
        PageantSummaryDTO pageant = service.startPageant(id);
        return new ResponseEntity<>(pageant, HttpStatus.OK);
    }

    @PostMapping("/pageants/{id}/finalize")
    public ResponseEntity<PageantSummaryDTO> finalizePageant(@PathVariable UUID id) {
        PageantSummaryDTO pageant = service.finalizePageant(id);
        return new ResponseEntity<>(pageant, HttpStatus.OK);
    }

    @PostMapping("/pageants/{id}/close")
    public ResponseEntity<PageantSummaryDTO> closePageant(@PathVariable UUID id) {
        PageantSummaryDTO pageant = service.closePageant(id);
        return new ResponseEntity<>(pageant, HttpStatus.OK);
    }

    @GetMapping("/pageants/{id}")
    public ResponseEntity<PageantSummaryDTO> getPageant(@PathVariable UUID id) {
        PageantSummaryDTO pageant = service.getPageant(id);
        return new ResponseEntity<>(pageant, HttpStatus.OK);
    }

    @GetMapping("/pageants")
    public ResponseEntity<List<PageantSummaryDTO>> getPageants() {
        List<PageantSummaryDTO> pageants = service.getPageants();
        return new ResponseEntity<>(pageants, HttpStatus.OK);
    }

    @PutMapping("/pageants/{id}")
    public ResponseEntity<PageantSummaryDTO> updatePageant(
            @PathVariable UUID id,
            @RequestBody @Valid PageantUpdateDTO pageantUpdateDTO
    ) {
        PageantSummaryDTO pageant = service.updatePageant(id, pageantUpdateDTO);
        return new ResponseEntity<>(pageant, HttpStatus.OK);
    }

    @DeleteMapping("/pageants/{id}")
    public ResponseEntity<PageantSummaryDTO> deletePageant(@PathVariable UUID id) {
        service.deletePageant(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
