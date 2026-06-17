package com.gdgocbu.tabulation.backend.controllers;

import com.gdgocbu.tabulation.backend.dtos.judge.JudgeSummaryDTO;
import com.gdgocbu.tabulation.backend.dtos.judge.JudgeUpdateDTO;
import com.gdgocbu.tabulation.backend.services.JudgeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class JudgeController {
    @Autowired
    private JudgeService service;

    @GetMapping("/judges/{id}")
    private ResponseEntity<JudgeSummaryDTO> getJudge(@PathVariable UUID id) {
        JudgeSummaryDTO judge = service.getJudge(id);
        return new ResponseEntity<>(judge, HttpStatus.OK);
    }

    @GetMapping("/judges")
    private ResponseEntity<List<JudgeSummaryDTO>> getJudges() {
        List<JudgeSummaryDTO> judges = service.getJudges();
        return new ResponseEntity<>(judges, HttpStatus.OK);
    }

    @PutMapping("/judges/{id}")
    private ResponseEntity<JudgeSummaryDTO> getJudge(@PathVariable UUID id, @RequestBody @Valid JudgeUpdateDTO judgeUpdateDTO) {
        JudgeSummaryDTO judge = service.updateJudge(id, judgeUpdateDTO);
        return new ResponseEntity<>(judge, HttpStatus.OK);
    }

    @DeleteMapping("/judges/{id}")
    private ResponseEntity<Void> deleteJudge(@PathVariable UUID id) {
        service.deleteJudge(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
