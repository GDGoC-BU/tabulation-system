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
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorColumn(name = "owner")
@Getter
@Setter
public abstract class Leaderboard {
    @Column(name = "owner", insertable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private String owner;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Embedded
    @Column(nullable = false)
    private Formula formula;

    @Column(nullable = false)
    private int leaderboardSelectionCount;

    @OneToMany(
            mappedBy = "leaderboard",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    protected List<LeaderboardEntry> leaderboard = new ArrayList<>();

    @Column(nullable = true)
    private LocalDateTime leaderboardLastCalculatedAt = null;

    public Leaderboard(Formula formula, int leaderboardSelectionCount) {
        this.formula = formula;
        this.leaderboardSelectionCount = leaderboardSelectionCount;
    }
}
