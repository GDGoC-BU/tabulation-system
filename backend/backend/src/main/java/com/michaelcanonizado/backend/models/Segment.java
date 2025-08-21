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
public class Segment {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "segment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Criteria> criteria = new ArrayList<>();

    public Segment(String name) {
        this.name = name;
    }

    public void addCriteria(Criteria criterion) {
        criteria.add(criterion);
        criterion.setSegment(this);
    }

    public void removeCriteria(Criteria criterion) {
        criteria.remove(criterion);
        criterion.setSegment(null);
    }
}
