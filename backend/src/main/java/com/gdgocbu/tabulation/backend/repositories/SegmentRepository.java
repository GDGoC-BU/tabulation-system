package com.gdgocbu.tabulation.backend.repositories;

import com.gdgocbu.tabulation.backend.models.Phase;
import com.gdgocbu.tabulation.backend.models.PhaseSegmentStatus;
import com.gdgocbu.tabulation.backend.models.Segment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SegmentRepository extends JpaRepository<Segment, UUID>, JpaSpecificationExecutor<Segment> {
    Optional<Segment> findBySequence(int sequence);
    List<Segment> findAllByStatusAndPhasePageantId(PhaseSegmentStatus status, UUID pageantId);
    Optional<Segment> findByStatus(PhaseSegmentStatus status);
}
