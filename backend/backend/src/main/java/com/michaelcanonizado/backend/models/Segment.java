package com.michaelcanonizado.backend.models;

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
public class Segment {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @JsonManagedReference
    @OneToMany(
            mappedBy = "segment",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Criterion> criteria = new ArrayList<>();


    /* Temporary list. CandidateSegmentQualifications is just an
    associative table for the many-to-many relationship of Candidates
    and Segments. We still need the real Candidate object with their
    details. Exclude list from getter and expose a separate getter
    stream to extract the actual candidate data: getQualifiedCandidates() */
    @JsonManagedReference
    @OneToMany(
            mappedBy = "segment",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Getter(AccessLevel.NONE)
    private List<CandidateSegmentQualification> candidateSegmentQualifications = new ArrayList<>();

    public Segment(String name) {
        this.name = name;
    }

    public void addCriterion(Criterion criterion) {
        criteria.add(criterion);
        criterion.setSegment(this);
    }
    public void removeCriterion(Criterion criterion) {
        criteria.remove(criterion);
        criterion.setSegment(null);
    }

    public void addCandidateSegmentQualification(CandidateSegmentQualification candidateSegmentQualification) {
        candidateSegmentQualifications.add(candidateSegmentQualification);
        candidateSegmentQualification.setSegment(this);
    }
    public void removeCandidateSegmentQualification(CandidateSegmentQualification candidateSegmentQualification) {
        candidateSegmentQualifications.remove(candidateSegmentQualification);
        candidateSegmentQualification.setSegment(null);
    }

    /* Infer the actual candidate data from the associative table */
    public List<Candidate> getQualifiedCandidates() {
        return candidateSegmentQualifications
                .stream()
                .filter(CandidateSegmentQualification::isQualified)
                .map((CandidateSegmentQualification::getCandidate))
                .toList();
    }
}
