package com.gdgocbu.tabulation.backend.repositories;

import com.gdgocbu.tabulation.backend.models.Phase;
import com.gdgocbu.tabulation.backend.models.PhaseSegmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PhaseRepository extends JpaRepository<Phase, UUID> {
    Optional<Phase> findByStatus(PhaseSegmentStatus status);
    List<Phase> findAllByStatusAndPageantId(PhaseSegmentStatus status, UUID pageantId);
    List<Phase> findAllByPageant_Id(UUID pageantId);
}
