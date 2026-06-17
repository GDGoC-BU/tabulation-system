package com.gdgocbu.tabulation.backend.repositories;

import com.gdgocbu.tabulation.backend.models.Criterion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface CriterionRepository extends JpaRepository<Criterion, UUID>, JpaSpecificationExecutor<Criterion> {
}
