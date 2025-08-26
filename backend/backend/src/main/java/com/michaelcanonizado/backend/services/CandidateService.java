package com.michaelcanonizado.backend.services;

import com.michaelcanonizado.backend.dtos.candidate.CandidateCreateDTO;
import com.michaelcanonizado.backend.dtos.candidate.CandidateSummaryDTO;
import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.entity.EntityNotFoundException;
import com.michaelcanonizado.backend.mappers.CandidateMapper;
import com.michaelcanonizado.backend.models.Candidate;
import com.michaelcanonizado.backend.models.College;
import com.michaelcanonizado.backend.repositories.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class CandidateService {
    @Autowired
    private CandidateRepository repository;
    @Autowired
    private CandidateMapper mapper;

    public void addCandidate(CandidateCreateDTO candidateCreateDTO) {
        Candidate candidate = mapper.toEntity(candidateCreateDTO);
        repository.save(candidate);
    }

    public CandidateSummaryDTO getCandidate(UUID id) {
        Candidate candidate = repository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Candidate not found!", ErrorCode.CANDIDATE_NOT_FOUND);
        });
        return mapper.toSummaryDTO(candidate);
    }

    public List<CandidateSummaryDTO> getCandidates() {
        List<Candidate> candidates = repository.findAll();
        return candidates
                .stream()
                .sorted(Comparator.comparing(Candidate::getNumber))
                .map(mapper::toSummaryDTO)
                .toList();
    }
}
