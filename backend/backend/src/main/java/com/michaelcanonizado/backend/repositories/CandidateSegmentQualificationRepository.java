package com.michaelcanonizado.backend.repositories;

import com.michaelcanonizado.backend.models.CandidateSegmentQualification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface CandidateSegmentQualificationRepository extends JpaRepository<CandidateSegmentQualification, UUID> {
}
