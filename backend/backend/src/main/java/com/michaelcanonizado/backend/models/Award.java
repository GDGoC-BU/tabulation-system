package com.michaelcanonizado.backend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Award {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int candidateLimit;

    @Column(nullable = false)
    private String formula;

    @JsonBackReference
    @ManyToOne(
            optional = false,
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "pageant_id", nullable = false)
    private Pageant pageant;

    @OneToMany(
            mappedBy = "award",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private List<AwardLeaderboard> leaderboard = new ArrayList<>();

    public Award(String name, int candidateLimit, String formula, Pageant pageant) {
        this.name = name;
        this.candidateLimit = candidateLimit;
        this.formula = formula;
        this.pageant = pageant;
    }

    public void addAwardLeaderboard(AwardLeaderboard awardLeaderboard) {
        leaderboard.add(awardLeaderboard);
        awardLeaderboard.setAward(this);
    }
    public void removeAwardLeaderboard(AwardLeaderboard awardLeaderboard) {
        leaderboard.remove(awardLeaderboard);
        awardLeaderboard.setAward(null);
    }
}
