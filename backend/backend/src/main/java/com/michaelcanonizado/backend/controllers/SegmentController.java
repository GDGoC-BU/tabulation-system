package com.michaelcanonizado.backend.controllers;

import com.michaelcanonizado.backend.dtos.segment.SegmentDetailedDTO;
import com.michaelcanonizado.backend.services.SegmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/api/v1/")
public class SegmentController {
    @Autowired
    private SegmentService service;

    @GetMapping("/segments/{id}")
    public ResponseEntity<SegmentDetailedDTO> getSegment(@PathVariable UUID id) {
        SegmentDetailedDTO segment = service.getSegment(id);
        return new ResponseEntity<>(segment, HttpStatus.OK);
    }
}
