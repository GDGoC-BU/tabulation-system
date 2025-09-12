package com.michaelcanonizado.backend.repositories;

import com.michaelcanonizado.backend.models.Phase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PhaseRepository extends JpaRepository<Phase, UUID> {
    List<Phase> findAllByPageant_Id(UUID pageantId);
}
