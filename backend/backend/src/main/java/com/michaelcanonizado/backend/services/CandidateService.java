package com.michaelcanonizado.backend.services;

import com.michaelcanonizado.backend.dtos.candidate.CandidateCreateDTO;
import com.michaelcanonizado.backend.dtos.candidate.CandidateSummaryDTO;
import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.entity.EntityMismatchException;
import com.michaelcanonizado.backend.exceptions.entity.EntityNotFoundException;
import com.michaelcanonizado.backend.mappers.CandidateMapper;
import com.michaelcanonizado.backend.models.Candidate;
import com.michaelcanonizado.backend.models.CandidateSegmentQualification;
import com.michaelcanonizado.backend.models.College;
import com.michaelcanonizado.backend.models.Segment;
import com.michaelcanonizado.backend.repositories.CandidateRepository;
import com.michaelcanonizado.backend.repositories.CandidateSegmentQualificationRepository;
import com.michaelcanonizado.backend.repositories.SegmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private CandidateMapper mapper;

    public CandidateSummaryDTO addCandidate(CandidateCreateDTO candidateCreateDTO) {
        /* Load DTO to Entity */
        Candidate candidate = mapper.toEntity(candidateCreateDTO);
        Candidate savedCandidate = candidateRepository.save(candidate);

        /* Get available segments and qualify
           the new candidate to each segment */
        List<Segment> segments = segmentRepository.findAll();
        segments.forEach(segment -> {
            csqRepository.save(new CandidateSegmentQualification(segment, savedCandidate));
        });

        /* Save candidate to database */
        return mapper.toSummaryDTO(savedCandidate);
    }

    public CandidateSummaryDTO getCandidate(UUID id) {
        Candidate candidate = candidateRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Candidate not found!", ErrorCode.CANDIDATE_NOT_FOUND);
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

    public CandidateSummaryDTO updateCandidate(UUID id, CandidateSummaryDTO candidateSummaryDTO) {
        if (!id.equals(candidateSummaryDTO.id())) {
            throw new EntityMismatchException(
                    "Path id " + id + " and Body.id " + candidateSummaryDTO.id() + " doesn't match.",
                    ErrorCode.CANDIDATE_MISMATCH
            );
        }

        Candidate candidate = candidateRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Candidate of id " + id + " doesn't exist.", ErrorCode.CANDIDATE_NOT_FOUND);
        });

        mapper.updateEntityFromDTO(candidate, candidateSummaryDTO);
        return mapper.toSummaryDTO(candidateRepository.save(candidate));
    }

    @Transactional
    public void deleteCandidate(UUID id) {
        Candidate candidate = candidateRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Candidate of id " + id + " doesn't exist.", ErrorCode.CANDIDATE_NOT_FOUND);
        });
        College college = candidate.getCollege();
        college.removeCandidate(candidate);
        candidateRepository.delete(candidate);
    }
}
