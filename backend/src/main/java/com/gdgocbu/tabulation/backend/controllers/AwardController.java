package com.gdgocbu.tabulation.backend.controllers;

import com.gdgocbu.tabulation.backend.dtos.award.AwardDetailedDTO;
import com.gdgocbu.tabulation.backend.dtos.award.AwardCreateDTO;
import com.gdgocbu.tabulation.backend.dtos.award.AwardSummaryDTO;
import com.gdgocbu.tabulation.backend.dtos.award.AwardUpdateDTO;
import com.gdgocbu.tabulation.backend.services.AwardService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1")
public class AwardController {
    @Autowired
    private AwardService service;

    @PostMapping("/awards")
    public ResponseEntity<AwardSummaryDTO> addAward(@RequestBody @Valid AwardCreateDTO awardCreateDTO) {
        AwardSummaryDTO award = service.addAward(awardCreateDTO);
        return new ResponseEntity<>(award, HttpStatus.CREATED);
    }
    
    @PostMapping("/awards/{id}/leaderboard/calculate")
    public ResponseEntity<Void> calculateLeaderboard(@PathVariable UUID id) {
        service.calculateLeaderboard(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/awards/{id}")
    public ResponseEntity<AwardDetailedDTO> getAward(@PathVariable UUID id) {
        AwardDetailedDTO award = service.getAward(id);
        return new ResponseEntity<>(award, HttpStatus.OK);
    }

    @GetMapping("/awards")
    public ResponseEntity<List<AwardSummaryDTO>> getAwards() {
        List<AwardSummaryDTO> awards = service.getAwards();
        return new ResponseEntity<>(awards, HttpStatus.OK);
    }

    @PutMapping("/awards/{id}")
    public ResponseEntity<AwardSummaryDTO> updateAward(
            @PathVariable UUID id,
            @RequestBody @Valid AwardUpdateDTO awardUpdateDTO
    ) {
        AwardSummaryDTO award = service.updateAward(id, awardUpdateDTO);
        return new ResponseEntity<>(award, HttpStatus.OK);
    }
}
