package com.michaelcanonizado.backend.controllers;

import com.michaelcanonizado.backend.dtos.pageant.PageantCreateDTO;
import com.michaelcanonizado.backend.dtos.pageant.PageantSummaryDTO;
import com.michaelcanonizado.backend.models.Pageant;
import com.michaelcanonizado.backend.services.PageantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
