package com.michaelcanonizado.backend.controllers;

import com.michaelcanonizado.backend.dtos.score.ScoreSummaryDTO;
import com.michaelcanonizado.backend.dtos.score.ScoreUpdateDTO;
import com.michaelcanonizado.backend.services.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/api/v1")
public class ScoreController {
    @Autowired
    private ScoreService service;

    @PutMapping("/scores/{id}")
    private ResponseEntity<ScoreSummaryDTO> updateScore(@PathVariable UUID id, @RequestBody ScoreUpdateDTO scoreUpdateDTO) {
        ScoreSummaryDTO score = service.updateScore(id, scoreUpdateDTO);
        return new ResponseEntity<>(score, HttpStatus.OK);
    }
}
