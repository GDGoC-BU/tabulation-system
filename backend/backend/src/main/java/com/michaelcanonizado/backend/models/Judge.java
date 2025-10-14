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

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Judge extends Account {
    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Honorific honorific;

    @JsonBackReference
    @ManyToOne(
            optional = false,
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "pageant_id", nullable = false)
    private Pageant pageant;

    @JsonManagedReference
    @OneToMany(
            mappedBy = "judge",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Score> scores  = new ArrayList<>();

    public Judge(String username, String passwordHash, String firstName, String lastName, Honorific honorific, Pageant pageant) {
        super(username, passwordHash);
        this.pageant = pageant;
        this.firstName = firstName;
        this.lastName = lastName;
        this.honorific = honorific;
    }

    public void addScore(Score score) {
        scores.add(score);
        score.setJudge(this);
    }
    public void removeScore(Score score) {
        scores.remove(score);
        score.setJudge(null);
    }
}
