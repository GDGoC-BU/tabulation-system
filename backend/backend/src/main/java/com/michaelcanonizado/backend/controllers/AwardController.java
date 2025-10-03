package com.michaelcanonizado.backend.controllers;

import com.michaelcanonizado.backend.dtos.AwardLeaderboardSummaryDTO;
import com.michaelcanonizado.backend.dtos.award.AwardCreateDTO;
import com.michaelcanonizado.backend.dtos.award.AwardSummaryDTO;
import com.michaelcanonizado.backend.services.AwardService;
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
public class AwardController {
    @Autowired
    private AwardService service;

    @PostMapping("/awards")
    public ResponseEntity<AwardSummaryDTO> addAward(@RequestBody @Valid AwardCreateDTO awardCreateDTO) {
        AwardSummaryDTO award = service.addAward(awardCreateDTO);
        return new ResponseEntity<>(award, HttpStatus.CREATED);
    }

    /* TEMPORARY ENDPOINT */
    @GetMapping("/awards/{id}/results")
    public ResponseEntity<List<AwardLeaderboardSummaryDTO>> getAwardResults(@PathVariable UUID id) {
        List<AwardLeaderboardSummaryDTO> leaderboard = service.getAwardResult(id);
        return new ResponseEntity<>(leaderboard, HttpStatus.OK);
    }

    @GetMapping("/awards")
    public ResponseEntity<List<AwardSummaryDTO>> getAwards() {
        List<AwardSummaryDTO> awards = service.getAwards();
        return new ResponseEntity<>(awards, HttpStatus.OK);
    }
}
