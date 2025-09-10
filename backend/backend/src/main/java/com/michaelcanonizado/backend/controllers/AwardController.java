package com.michaelcanonizado.backend.controllers;

import com.michaelcanonizado.backend.dtos.award.AwardCreateDTO;
import com.michaelcanonizado.backend.dtos.award.AwardSummaryDTO;
import com.michaelcanonizado.backend.services.AwardService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

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

    @GetMapping("/awards")
    public ResponseEntity<List<AwardSummaryDTO>> getAward() {
        List<AwardSummaryDTO> awards = service.getAwards();
        return new ResponseEntity<>(awards, HttpStatus.CREATED);
    }
}
