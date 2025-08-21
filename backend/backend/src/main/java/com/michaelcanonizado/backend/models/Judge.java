package com.michaelcanonizado.backend.models;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Judge extends Auditable {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String passwordHash;

    @Column(nullable = false, unique = true)
    private boolean isOnline = false;

    @Column(nullable = true)
    private Instant lastSeenAt;

    public Judge(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
    }
}
