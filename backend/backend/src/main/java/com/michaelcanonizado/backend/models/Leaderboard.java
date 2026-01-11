package com.michaelcanonizado.backend.models;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Leaderboard {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Embedded
    @Column(nullable = false)
    private Formula formula;

    @Column(nullable = false)
    private int selectionCount;

    @OneToMany(
            mappedBy = "leaderboard",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    protected List<LeaderboardEntry> entries = new ArrayList<>();

    @Column(nullable = true)
    private LocalDateTime lastCalculatedAt = null;

    public Leaderboard(Formula formula, int selectionCount) {
        this.formula = formula;
        this.selectionCount = selectionCount;
    }
}
