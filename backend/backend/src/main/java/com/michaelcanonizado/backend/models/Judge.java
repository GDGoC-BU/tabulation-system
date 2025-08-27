package com.michaelcanonizado.backend.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Judge extends Account {
    /* ADD HELPER METHODS TO ADD AND REMOVE SCORES */
    @JsonManagedReference
    @OneToMany(
            mappedBy = "judge",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Score> scores  = new ArrayList<>();

    public Judge(String username, String passwordHash) {
        super(username, passwordHash);
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
