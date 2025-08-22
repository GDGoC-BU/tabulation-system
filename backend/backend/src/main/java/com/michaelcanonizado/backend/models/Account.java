package com.michaelcanonizado.backend.models;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Account extends Auditable {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String passwordHash;

    @Column(nullable = false)
    private boolean isOnline = false;

    @Column(nullable = true)
    private Instant lastSeenAt;

    public Account(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
    }
}
