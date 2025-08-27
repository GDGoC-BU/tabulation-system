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
@Getter
@Setter
public class CandidateSegmentQualification {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(nullable = false)
    private boolean isQualified = true;

    @JsonBackReference
    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;

    @JsonBackReference
    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "segment_id", nullable = false)
    private Segment segment;

    public CandidateSegmentQualification(Segment segment, Candidate candidate) {
        this.segment = segment;
        this.candidate = candidate;
    }
}
