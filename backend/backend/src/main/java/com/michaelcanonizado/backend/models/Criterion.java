package com.michaelcanonizado.backend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Criterion {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int maxScore;

    @JsonBackReference
    @ManyToOne(
            optional = false,
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "segment_id", nullable = false)
    private Segment segment;

    @JsonManagedReference
    @OneToMany(
            mappedBy = "criterion",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Score> scores = new ArrayList<>();

    public Criterion(String name, int maxScore, Segment segment) {
        this.name = name;
        this.maxScore = maxScore;
        this.segment = segment;
    }

    public void addScore(Score score) {
        scores.add(score);
        score.setCriterion(this);
    }
    public void removeScore(Score score) {
        scores.remove(score);
        score.setCriterion(null);
    }
}
