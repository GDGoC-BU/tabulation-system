package com.michaelcanonizado.backend.repositories;

import com.michaelcanonizado.backend.models.Judge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JudgeRepository extends JpaRepository<Judge, UUID> {
    List<Judge> findAllByPageant_Id(UUID pageantId);

}
