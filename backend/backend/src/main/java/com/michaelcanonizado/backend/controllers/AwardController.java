package com.michaelcanonizado.backend.controllers;

import com.michaelcanonizado.backend.dtos.award.AwardCreateDTO;
import com.michaelcanonizado.backend.dtos.award.AwardSummaryDTO;
import com.michaelcanonizado.backend.services.AwardService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/v1")
public class AwardController {
    @Autowired
    private AwardService service;

    @PostMapping("/awards")
    public ResponseEntity<AwardSummaryDTO> addAward(@RequestBody @Valid AwardCreateDTO awardCreateDTO) {
        AwardSummaryDTO award = service.addAward(awardCreateDTO);
        return new ResponseEntity<>(award, HttpStatus.CREATED);
    }
}
