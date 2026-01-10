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

    /* A constraint should be placed in candidateLimit and formula!
       Enforce data integrity in the entities and database!

       Also add some logic to check that each segment has a funnel effect
       on the candidate limit. I.e: candidate limit of the current segment
       should be less than or equal to the previous segment.

       Furthermore, get the candidates who are qualified on the previous segment,
       not all candidates. */
    @Column(nullable = true)
    private Integer candidateLimit;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(
                    name = "text",
                    column = @Column(
                            name = "text",
                            nullable = true
                    )
            ),
            @AttributeOverride(
                    name = "workspace",
                    column = @Column(
                            name = "workspace",
                            columnDefinition = "jsonb",
                            nullable = true
                    )
            )
    })
    private Formula formula;

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
    private List<CandidateSegmentQualification> candidateQualifications = new ArrayList<>();

    public Segment(String name, int sequence, Integer candidateLimit, Formula formula, Phase phase) {
        this.name = name;
        this.sequence = sequence;
        this.candidateLimit = candidateLimit;
        this.formula = formula;
        this.phase = phase;
    }

    public void addCriterion(Criterion criterion) {
        criteria.add(criterion);
        criterion.setSegment(this);
    }
    public void removeCriterion(Criterion criterion) {
        criteria.remove(criterion);
        criterion.setSegment(null);
    }

    public void addCandidateQualification(CandidateSegmentQualification csq) {
        candidateQualifications.add(csq);
        csq.setSegment(this);
    }
    public void removeCandidateQualification(CandidateSegmentQualification csq) {
        candidateQualifications.remove(csq);
        csq.setSegment(null);
    }

    /* Infer the actual candidate data from the associative table */
//    public List<Candidate> getQualifiedCandidates() {
//        return candidateSegmentQualifications
//                .stream()
//                .filter(CandidateSegmentQualification::isQualified)
//                .map((CandidateSegmentQualification::getCandidate))
//                .toList();
//    }
}
