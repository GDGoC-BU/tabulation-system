package com.michaelcanonizado.backend.models;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Pageant extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PageantStatus status = PageantStatus.PREPARATION;

    @Column(nullable = false, unique = true)
    private String title;

    @Column(nullable = true)
    private LocalDateTime startedAt;

    @Column(nullable = true)
    private LocalDateTime endedAt;

    public Pageant(String title) {
        this.title = title;
    }
}
