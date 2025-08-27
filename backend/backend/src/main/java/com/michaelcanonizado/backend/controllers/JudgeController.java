package com.michaelcanonizado.backend.controllers;

import com.michaelcanonizado.backend.dtos.judge.JudgeCreateDTO;
import com.michaelcanonizado.backend.dtos.judge.JudgeDetailedDTO;
import com.michaelcanonizado.backend.dtos.judge.JudgeUpdateDTO;
import com.michaelcanonizado.backend.services.JudgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api/v1")
public class JudgeController {
    @Autowired
    private JudgeService service;

    @PostMapping("/judges")
    private ResponseEntity<JudgeDetailedDTO> addJudge(@RequestBody JudgeCreateDTO judgeCreateDTO) {
        JudgeDetailedDTO judge = service.addJudge(judgeCreateDTO);
        return new ResponseEntity<>(judge, HttpStatus.CREATED);
    }

    @GetMapping("/judges/{id}")
    private ResponseEntity<JudgeDetailedDTO> getJudge(@PathVariable UUID id) {
        JudgeDetailedDTO judge = service.getJudge(id);
        return new ResponseEntity<>(judge, HttpStatus.OK);
    }

    @GetMapping("/judges")
    private ResponseEntity<List<JudgeDetailedDTO>> getJudges() {
        List<JudgeDetailedDTO> judges = service.getJudges();
        return new ResponseEntity<>(judges, HttpStatus.OK);
    }

    @PutMapping("/judges/{id}")
    private ResponseEntity<JudgeDetailedDTO> getJudge(@PathVariable UUID id, @RequestBody JudgeUpdateDTO judgeUpdateDTO) {
        JudgeDetailedDTO judge = service.updateJudge(id, judgeUpdateDTO);
        return new ResponseEntity<>(judge, HttpStatus.OK);
    }

    @DeleteMapping("/judges/{id}")
    private ResponseEntity<Void> deleteJudge(@PathVariable UUID id) {
        service.deleteJudge(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
