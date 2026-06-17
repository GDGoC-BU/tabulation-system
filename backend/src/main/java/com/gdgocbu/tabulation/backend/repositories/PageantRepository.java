package com.gdgocbu.tabulation.backend.repositories;

import com.gdgocbu.tabulation.backend.models.Pageant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PageantRepository extends JpaRepository<Pageant, UUID> {
}
