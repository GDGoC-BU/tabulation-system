package com.gdgocbu.tabulation.backend.controllers;

import com.gdgocbu.tabulation.backend.dtos.score.ScoreDetailedDTO;
import com.gdgocbu.tabulation.backend.dtos.score.ScoreUpdateDTO;
import com.gdgocbu.tabulation.backend.services.ScoreService;
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
public class ScoreController {
    @Autowired
    private ScoreService service;

    @GetMapping("/scores")
    public ResponseEntity<List<ScoreDetailedDTO>> getScores(
            @RequestParam(required = false) UUID judgeId,
            @RequestParam(required = false) UUID candidateId,
            @RequestParam(required = false) UUID criterionId,
            @RequestParam(required = false) UUID segmentId

    ) {
        List<ScoreDetailedDTO> scores = service.getScores(
                judgeId,
                candidateId,
                criterionId,
                segmentId
        );
        return new ResponseEntity<>(scores, HttpStatus.OK);
    }

    @PutMapping("/scores/{id}")
    private ResponseEntity<ScoreDetailedDTO> updateScore(@PathVariable UUID id, @RequestBody @Valid ScoreUpdateDTO scoreUpdateDTO) {
        ScoreDetailedDTO score = service.updateScore(id, scoreUpdateDTO);
        return new ResponseEntity<>(score, HttpStatus.OK);
    }
}
