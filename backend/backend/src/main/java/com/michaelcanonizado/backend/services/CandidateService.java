package com.michaelcanonizado.backend.services;

import com.michaelcanonizado.backend.dtos.candidate.CandidateCreateDTO;
import com.michaelcanonizado.backend.dtos.candidate.CandidateSummaryDTO;
import com.michaelcanonizado.backend.dtos.candidate.CandidateUpdateDTO;
import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.entity.EntityNotFoundException;
import com.michaelcanonizado.backend.mappers.CandidateMapper;
import com.michaelcanonizado.backend.models.*;
import com.michaelcanonizado.backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class CandidateService {
    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private CandidateSegmentQualificationRepository csqRepository;

    @Autowired
    private SegmentRepository segmentRepository;

    @Autowired
    private JudgeRepository judgeRepository;

    @Autowired
    private CriterionRepository criterionRepository;

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private CandidateMapper mapper;

    @Transactional
    public CandidateSummaryDTO addCandidate(CandidateCreateDTO candidateCreateDTO) {
        /* Load DTO to Entity */
        Candidate candidate = mapper.toEntity(candidateCreateDTO);
        Candidate savedCandidate = candidateRepository.save(candidate);

        /* Get available segments and qualify
           the new candidate to each segment */
        List<Segment> segments = segmentRepository.findAll();
        List<CandidateSegmentQualification> newCSQs = new ArrayList<>();
        segments.forEach(segment -> {
            newCSQs.add(new CandidateSegmentQualification(segment, savedCandidate));
        });
        /* Batch save to minimize insert queries */
        csqRepository.saveAll(newCSQs);

        /* Pre-generate the scores for the new candidate */
        List<Judge> judges = judgeRepository.findAll();
        List<Criterion> criteria = criterionRepository.findAll();
        List<Score> newScores = new ArrayList<>();
        judges.forEach(judge -> {
            criteria.forEach(criterion -> {
                newScores.add(new Score(0, judge, savedCandidate, criterion));
            });
        });
        /* Batch save to minimize insert queries */
        scoreRepository.saveAll(newScores);

        /* Save candidate to database */
        return mapper.toSummaryDTO(savedCandidate);
    }

    public CandidateSummaryDTO getCandidate(UUID id) {
        Candidate candidate = candidateRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Candidate not found!", ErrorCode.ENTITY_NOT_FOUND);
        });
        return mapper.toSummaryDTO(candidate);
    }

    public List<CandidateSummaryDTO> getCandidates() {
        List<Candidate> candidates = candidateRepository.findAll();
        return candidates
                .stream()
                .sorted(Comparator.comparing(Candidate::getNumber))
                .map(mapper::toSummaryDTO)
                .toList();
    }

    public CandidateSummaryDTO updateCandidate(UUID id, CandidateUpdateDTO candidateUpdateDTO) {
        Candidate candidate = candidateRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Can't update! Candidate not found.", ErrorCode.ENTITY_NOT_FOUND);
        });

        mapper.updateEntityFromDTO(candidate, candidateUpdateDTO);
        return mapper.toSummaryDTO(candidateRepository.save(candidate));
    }

    @Transactional
    public void deleteCandidate(UUID id) {
        Candidate candidate = candidateRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Can't delete! Candidate not found.", ErrorCode.ENTITY_NOT_FOUND);
        });
        College college = candidate.getCollege();
        college.removeCandidate(candidate);
        candidateRepository.delete(candidate);
    }
}
