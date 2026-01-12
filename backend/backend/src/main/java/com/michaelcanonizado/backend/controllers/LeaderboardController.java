package com.michaelcanonizado.backend.controllers;

import com.michaelcanonizado.backend.dtos.leaderboard.LeaderboardDetailedDTO;
import com.michaelcanonizado.backend.services.LeaderboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("api/v1")
public class LeaderboardController {
    @Autowired
    private LeaderboardService service;

    @GetMapping("/leaderboard/{id}")
    public ResponseEntity<LeaderboardDetailedDTO> getLeaderboard(@PathVariable UUID id) {
        LeaderboardDetailedDTO account = service.getLeaderboard(id);
        return new ResponseEntity<>(account, HttpStatus.OK);
    }
}
