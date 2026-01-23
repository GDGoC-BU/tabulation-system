package com.michaelcanonizado.backend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Segment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private UUID id;

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

    @OneToOne(
            optional = true,
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "qualification_leaderboard_id")
    private Leaderboard qualificationLeaderboard;

    @OneToOne(
            optional = true,
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "ranking_leaderboard_id")
    private Leaderboard rankingLeaderboard;

    public Segment(
            String name,
            int sequence,
            Phase phase
    ) {
        this.name = name;
        this.sequence = sequence;
        this.phase = phase;
    }

    public Segment getPreviousSegment() {
        return phase
                .getSegments()
                .stream()
                .filter(s -> s.getSequence() < this.sequence)
                .max(Comparator.comparingInt(Segment::getSequence))
                .orElse(null);
    }

}
