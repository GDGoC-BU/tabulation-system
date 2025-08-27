package com.michaelcanonizado.backend.controllers;

import com.michaelcanonizado.backend.dtos.score.ScoreSummaryDTO;
import com.michaelcanonizado.backend.dtos.score.ScoreUpdateDTO;
import com.michaelcanonizado.backend.services.ScoreService;
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
    public ResponseEntity<List<ScoreSummaryDTO>> getScores(
            @RequestParam(required = false) UUID judgeId,
            @RequestParam(required = false) UUID candidateId,
            @RequestParam(required = false) UUID criterionId
    ) {
        List<ScoreSummaryDTO> scores = service.getScores(judgeId, candidateId, criterionId);
        return new ResponseEntity<>(scores, HttpStatus.OK);
    }

    @PutMapping("/scores/{id}")
    private ResponseEntity<ScoreSummaryDTO> updateScore(@PathVariable UUID id, @RequestBody ScoreUpdateDTO scoreUpdateDTO) {
        ScoreSummaryDTO score = service.updateScore(id, scoreUpdateDTO);
        return new ResponseEntity<>(score, HttpStatus.OK);
    }
}
