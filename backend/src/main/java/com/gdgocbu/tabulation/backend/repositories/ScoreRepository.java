package com.gdgocbu.tabulation.backend.repositories;

import com.gdgocbu.tabulation.backend.models.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface ScoreRepository extends JpaRepository<Score, UUID>, JpaSpecificationExecutor<Score> {
    @Modifying(
            flushAutomatically = true,
            clearAutomatically = true
    )
    @Query("""
    DELETE FROM Score s
    WHERE s.candidate.pageant.id = :pageantId
    """)
    void deleteByPageantId(@Param("pageantId") UUID pageantId);
}
