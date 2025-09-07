package com.michaelcanonizado.backend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
public class Candidate extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(nullable = false)
    private int number;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CandidateGender candidateGender;

    @Column(nullable = false)
    private int age;

    @JsonBackReference
    @ManyToOne(
            optional = false,
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "college_id", nullable = false)
    private College college;

    /* Temporary list. Still need the real Segment object
    with their details. Exclude list from getter and expose a
    separate getter stream to extract the actual segment data:
    getQualifiedSegments() */
    @JsonManagedReference
    @OneToMany(
            mappedBy = "candidate",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Getter(AccessLevel.NONE)
    private List<CandidateSegmentQualification> qualifiedSegments = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(
            mappedBy = "candidate",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Score> scores = new ArrayList<>();

    public Candidate(int number, String firstName, String lastName, CandidateGender candidateGender, int age, College college) {
        this.number = number;
        this.firstName = firstName;
        this.lastName = lastName;
        this.candidateGender = candidateGender;
        this.age = age;
        this.college = college;
    }

    public void addCandidateSegmentQualification(CandidateSegmentQualification candidateSegmentQualification) {
        qualifiedSegments.add(candidateSegmentQualification);
        candidateSegmentQualification.setCandidate(this);
    }
    public void removeCandidateSegmentQualification(CandidateSegmentQualification candidateSegmentQualification) {
        qualifiedSegments.remove(candidateSegmentQualification);
        candidateSegmentQualification.setCandidate(null);
    }
    /* TEST THIS. I DON'T THINK THIS IS NECESSARY BECAUSE JPA ALREADY LOADS THE RELATIONSHIP */
    public List<Segment> getQualifiedSegments() {
        return qualifiedSegments.stream().map((CandidateSegmentQualification::getSegment)).toList();
    }
}
