package com.gdgocbu.tabulation.backend.controllers;

import com.gdgocbu.tabulation.backend.dtos.leaderboard.LeaderboardDetailedDTO;
import com.gdgocbu.tabulation.backend.dtos.segment.SegmentCreateDTO;
import com.gdgocbu.tabulation.backend.dtos.segment.SegmentDetailedDTO;
import com.gdgocbu.tabulation.backend.dtos.segment.SegmentSummaryDTO;
import com.gdgocbu.tabulation.backend.dtos.segment.SegmentUpdateDTO;
import com.gdgocbu.tabulation.backend.services.SegmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/")
public class SegmentController {
    @Autowired
    private SegmentService service;

    @PostMapping("/segments")
    public ResponseEntity<SegmentDetailedDTO> addSegment(@RequestBody @Valid SegmentCreateDTO segmentCreateDTO) {
        SegmentDetailedDTO segment = service.addSegment(segmentCreateDTO);
        return new ResponseEntity<>(segment, HttpStatus.CREATED);
    }

    @PostMapping("/segments/{id}/start")
    public ResponseEntity<SegmentDetailedDTO> startSegment(@PathVariable UUID id) {
        SegmentDetailedDTO segment = service.startSegment(id);
        return new ResponseEntity<>(segment, HttpStatus.OK);
    }

    @PostMapping("/segments/{id}/close")
    public ResponseEntity<SegmentDetailedDTO> closeSegment(@PathVariable UUID id) {
        SegmentDetailedDTO segment = service.closeSegment(id);
        return new ResponseEntity<>(segment, HttpStatus.OK);
    }

    @PostMapping("/segments/{id}/qualificationLeaderboard/calculate")
    public ResponseEntity<LeaderboardDetailedDTO> calculateQualificationLeaderboard(@PathVariable UUID id) {
        LeaderboardDetailedDTO leaderboard = service.calculateQualificationLeaderboard(id);
        return new ResponseEntity<>(leaderboard, HttpStatus.OK);
    }

    @GetMapping("/segments/{id}/qualificationLeaderboard")
    public ResponseEntity<LeaderboardDetailedDTO> getQualificationLeaderboard(@PathVariable UUID id) {
        LeaderboardDetailedDTO leaderboard = service.getQualificationLeaderboard(id);
        return new ResponseEntity<>(leaderboard, HttpStatus.OK);
    }

    @GetMapping("/segments/{id}")
    public ResponseEntity<SegmentDetailedDTO> getSegment(@PathVariable UUID id) {
        SegmentDetailedDTO segment = service.getSegment(id);
        return new ResponseEntity<>(segment, HttpStatus.OK);
    }

    @GetMapping("/segments/ongoing")
    public ResponseEntity<SegmentDetailedDTO> getOngoingSegment() {
        SegmentDetailedDTO segment = service.getOngoingSegment();
        if (segment == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(segment, HttpStatus.OK);
    }

    @GetMapping("/segments")
    public ResponseEntity<List<SegmentSummaryDTO>> getSegments() {
        List<SegmentSummaryDTO> segments = service.getSegments();
        return new ResponseEntity<>(segments, HttpStatus.OK);
    }

    @PutMapping("/segments/{id}")
    public ResponseEntity<SegmentDetailedDTO> updateSegment(@PathVariable UUID id, @RequestBody @Valid SegmentUpdateDTO segmentUpdateDTO) {
        SegmentDetailedDTO segment = service.updateSegment(id, segmentUpdateDTO);
        return new ResponseEntity<>(segment, HttpStatus.OK);
    }

    @DeleteMapping("/segments/{id}")
    public ResponseEntity<Void> deleteSegment(@PathVariable UUID id) {
        service.deleteSegment(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
