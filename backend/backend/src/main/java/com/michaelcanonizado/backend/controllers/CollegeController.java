package com.michaelcanonizado.backend.controllers;

import com.michaelcanonizado.backend.models.College;
import com.michaelcanonizado.backend.services.CollegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/v1/")
public class CollegeController {
    @Autowired
    private CollegeService service;

    @GetMapping("/colleges")
    public ResponseEntity<List<College>> getColleges() {
        List<College> colleges = service.getColleges();
        return new ResponseEntity<>(colleges, HttpStatus.OK);
    }
}
