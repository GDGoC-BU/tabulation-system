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
    @OneToMany(mappedBy = "segment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Criterion> criteria = new ArrayList<>();


    /* Temporary list. Still need the real Candidate object
    with their details. Exclude list from getter and expose a
    separate getter stream to extract the actual candidate data:
    getQualifiedCandidate() */
    @JsonManagedReference
    @OneToMany(mappedBy = "segment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Getter(AccessLevel.NONE)
    private List<CandidateSegmentQualification> candidateSegmentQualifications = new ArrayList<>();

    public Segment(String name) {
        this.name = name;
    }

    public void addCriteria(Criterion criterion) {
        criteria.add(criterion);
        criterion.setSegment(this);
    }
    public void removeCriteria(Criterion criterion) {
        criteria.remove(criterion);
        criterion.setSegment(null);
    }

    /* WRONG LOGIC. JUST SET isQualified TO FALSE. OR ADD ANOTHER HELPER METHOD */
    public void addQualifiedCandidate(CandidateSegmentQualification candidateSegmentQualification) {
        candidateSegmentQualifications.add(candidateSegmentQualification);
        candidateSegmentQualification.setSegment(this);
    }
    public void removeQualifiedCandidate(CandidateSegmentQualification candidateSegmentQualification) {
        candidateSegmentQualifications.remove(candidateSegmentQualification);
        candidateSegmentQualification.setSegment(null);
    }
    /* TEST THIS. I DON'T THINK THIS IS NECESSARY BECAUSE JPA ALREADY LOADS THE RELATIONSHIP */
    public List<Candidate> getQualifiedCandidates() {
        return candidateSegmentQualifications
                .stream()
                .filter(CandidateSegmentQualification::isQualified)
                .map((CandidateSegmentQualification::getCandidate))
                .toList();
    }
}
