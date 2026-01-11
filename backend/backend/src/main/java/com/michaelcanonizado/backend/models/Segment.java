package com.michaelcanonizado.backend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Segment extends Leaderboard {
    @Column(nullable = false, unique = false)
    private String name;

    @Column(nullable = false)
    private int sequence;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PhaseSegmentStatus status = PhaseSegmentStatus.PENDING;

    @JsonBackReference
    @ManyToOne(
            optional = false,
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "phase_id", nullable = false)
    private Phase phase;

    @JsonManagedReference
    @OneToMany(
            mappedBy = "segment",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Criterion> criteria = new ArrayList<>();

    @Column(nullable = false)
    private boolean hasQualifications;

    public Segment(
            String name,
            int sequence,
            Phase phase,
            boolean hasQualifications,
            Formula formula,
            int leaderboardSelectionCount
    ) {
        super(formula, leaderboardSelectionCount);
        this.name = name;
        this.sequence = sequence;
        this.phase = phase;
        this.hasQualifications = hasQualifications;
    }
}
