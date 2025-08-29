package com.michaelcanonizado.backend.repositories;

import com.michaelcanonizado.backend.models.Pageant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PageantRepository extends JpaRepository<Pageant, UUID> {
    @Query("SELECT p FROM Pageant p")
    Optional<Pageant> findSingleton();

    boolean existsBy();
}
