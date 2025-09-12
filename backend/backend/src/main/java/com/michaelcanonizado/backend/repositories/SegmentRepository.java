package com.michaelcanonizado.backend.repositories;

import com.michaelcanonizado.backend.models.Segment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SegmentRepository extends JpaRepository<Segment, UUID> {
    @Query("SELECT s FROM Segment s JOIN FETCH s.phase p ORDER BY p.sequence, s.sequence")
    List<Segment> findAllOrderByPhaseSequenceAndSegmentSequence();
}
