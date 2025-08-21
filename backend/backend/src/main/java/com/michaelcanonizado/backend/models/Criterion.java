package com.michaelcanonizado.backend.models;

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
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int maxScore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "segment_id", nullable = false)
    private Segment segment;

    @OneToMany(mappedBy = "criterion", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Score> scores = new ArrayList<>();

    public Criterion(String name, int maxScore, Segment segment) {
        this.name = name;
        this.maxScore = maxScore;
        this.segment = segment;
    }
}
