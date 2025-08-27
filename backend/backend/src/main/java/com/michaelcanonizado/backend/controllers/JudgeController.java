package com.michaelcanonizado.backend.controllers;

import com.michaelcanonizado.backend.dtos.judge.JudgeCreateDTO;
import com.michaelcanonizado.backend.dtos.judge.JudgeSummaryDTO;
import com.michaelcanonizado.backend.services.JudgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1")
public class JudgeController {
    @Autowired
    private JudgeService service;

    @PostMapping("/judges")
    private ResponseEntity<JudgeSummaryDTO> addJudge(@RequestBody JudgeCreateDTO judgeCreateDTO) {
        JudgeSummaryDTO judge = service.addJudge(judgeCreateDTO);
        return new ResponseEntity<>(judge, HttpStatus.CREATED);
    }
}
