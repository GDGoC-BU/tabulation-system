package com.michaelcanonizado.backend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class LeaderboardEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private UUID id;

    @JsonBackReference
    @ManyToOne(
            optional = false,
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "leaderboard_id")
    private Leaderboard leaderboard;

    @JsonBackReference
    @ManyToOne(
            optional = false,
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;

    @Column(nullable = true)
    @Check(constraints = "rank IS NULL OR rank >= 1")
    private Integer rank;

    @Column(nullable = false, precision = 20, scale = 10)
    private BigDecimal score;

    @Column(nullable = false)
    boolean isOverridden = false;

    @Column(nullable = true)
    String overrideReason;

    @Column(nullable = false)
    private boolean isTied = false;

    @Column(nullable = false)
    private boolean isSelected = false;

    @Type(JsonBinaryType.class)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<CriteriaBreakdown> criteriaBreakdown = new ArrayList<>();

    public LeaderboardEntry(Candidate candidate, Integer rank, BigDecimal score) {
        this.candidate = candidate;
        this.rank = rank;
        this.score = score;
    }
}
