package com.michaelcanonizado.backend.services;

import com.michaelcanonizado.backend.dtos.candidate.CandidateCreateDTO;
import com.michaelcanonizado.backend.mappers.CandidateMapper;
import com.michaelcanonizado.backend.models.Candidate;
import com.michaelcanonizado.backend.repositories.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
