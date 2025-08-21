package com.michaelcanonizado.backend.models;

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
public class Candidate extends Auditable{
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(nullable = false)
    private int candidateNumber;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "college_id", nullable = false)
    private College college;

    /* Temporary list. Still need the real Segment object
    with their details. Exclude list from getter and expose a
    separate getter stream to extract the actual segment data:
    getQualifiedSegments() */
    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Getter(AccessLevel.NONE)
    private List<CandidateSegmentQualification> qualifiedSegments = new ArrayList<>();

    public Candidate(int candidateNumber, String firstName, String lastName, Gender gender, int age, College college) {
        this.candidateNumber = candidateNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.age = age;
        this.college = college;
    }

    public void addQualifiedCandidate(CandidateSegmentQualification candidateSegmentQualification) {
        qualifiedSegments.add(candidateSegmentQualification);
        candidateSegmentQualification.setCandidate(this);
    }
    public void removeQualifiedCandidate(CandidateSegmentQualification candidateSegmentQualification) {
        qualifiedSegments.remove(candidateSegmentQualification);
        candidateSegmentQualification.setCandidate(null);
    }
    public List<Segment> getQualifiedSegments() {
        return qualifiedSegments.stream().map((CandidateSegmentQualification::getSegment)).toList();
    }
}
