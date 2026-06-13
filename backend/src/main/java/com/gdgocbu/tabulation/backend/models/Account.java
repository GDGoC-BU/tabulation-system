package com.gdgocbu.tabulation.backend.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "account_type")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public abstract class Account extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String passwordHash;

    @Column(nullable = false)
    private boolean isOnline = false;

    @Column(nullable = true)
    private LocalDateTime lastSeenAt;

    @Column(name = "account_type", insertable = false, updatable = false)
    private String accountType;

    public Account(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
    }
}
