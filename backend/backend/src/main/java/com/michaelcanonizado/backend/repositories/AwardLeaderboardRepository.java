package com.michaelcanonizado.backend.repositories;

import com.michaelcanonizado.backend.models.AwardLeaderboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AwardLeaderboardRepository extends JpaRepository<AwardLeaderboard, UUID> {
}
