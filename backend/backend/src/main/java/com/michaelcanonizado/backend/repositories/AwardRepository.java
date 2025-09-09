package com.michaelcanonizado.backend.repositories;

import com.michaelcanonizado.backend.models.Award;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AwardRepository extends JpaRepository<Award, UUID> {
}
