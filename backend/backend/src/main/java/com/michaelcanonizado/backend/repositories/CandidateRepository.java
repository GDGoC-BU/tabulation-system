package com.michaelcanonizado.backend.repositories;

import com.michaelcanonizado.backend.models.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, UUID> {
    List<Candidate> findAllByPageant_Id(UUID pageantId);
}
