package com.michaelcanonizado.backend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        uniqueConstraints = @UniqueConstraint(
                columnNames = {
                        "candidate_id",
                        "award_id"
                }
        )
)
@Getter
@Setter
public class AwardLeaderboard {
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
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;

    @JsonBackReference
    @ManyToOne(
            optional = false,
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "award_id", nullable = false)
    private Award award;

    @Column(nullable = false)
    private int score;

    public AwardLeaderboard(int score, Candidate candidate, Award award) {
        this.candidate = candidate;
        this.award = award;
        this.score = score;
    }
}
