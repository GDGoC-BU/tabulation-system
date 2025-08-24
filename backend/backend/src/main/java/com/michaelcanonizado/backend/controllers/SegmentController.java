package com.michaelcanonizado.backend.controllers;

import com.michaelcanonizado.backend.dtos.segment.SegmentCreateDTO;
import com.michaelcanonizado.backend.dtos.segment.SegmentDetailedDTO;
import com.michaelcanonizado.backend.dtos.segment.SegmentSummaryDTO;
import com.michaelcanonizado.backend.services.SegmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping("/segments")
    public ResponseEntity<List<SegmentSummaryDTO>> getSegments() {
        List<SegmentSummaryDTO> segments = service.getSegments();
        return new ResponseEntity<>(segments, HttpStatus.OK);
    }

    @PostMapping("/segments")
    public ResponseEntity<SegmentDetailedDTO> addSegment(@RequestBody SegmentCreateDTO segmentCreateDTO) {
        SegmentDetailedDTO segment = service.addSegment(segmentCreateDTO);
        return new ResponseEntity<>(segment, HttpStatus.CREATED);
    }

    @PatchMapping("/segments/{id}")
    public ResponseEntity<SegmentSummaryDTO> updateSegment(@PathVariable UUID id, @RequestBody SegmentSummaryDTO segmentSummaryDTO) {
        SegmentSummaryDTO segment = service.updateSegment(id, segmentSummaryDTO);
        return new ResponseEntity<>(segment, HttpStatus.OK);
    }
}
