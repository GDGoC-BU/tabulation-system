package com.michaelcanonizado.backend.repositories;

import com.michaelcanonizado.backend.models.Criterion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface CriterionRepository extends JpaRepository<Criterion, UUID>, JpaSpecificationExecutor<Criterion> {
}
