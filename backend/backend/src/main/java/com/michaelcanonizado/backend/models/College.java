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
public class College {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false, unique = true)
    private String name;

    @JsonManagedReference
    @OneToMany(mappedBy = "college", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Candidate> candidates = new ArrayList<>();

    public College(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public void addCandidate(Candidate candidate) {
        candidates.add(candidate);
        candidate.setCollege(this);
    }

    public void removeCandidate(Candidate candidate) {
        candidates.remove(candidate);
        candidate.setCollege(null);
    }
}
