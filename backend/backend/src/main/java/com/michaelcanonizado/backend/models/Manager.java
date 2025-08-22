package com.michaelcanonizado.backend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Manager extends Account {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ManagerRole role;

    public Manager(String username, String passwordHash, ManagerRole role) {
        super(username, passwordHash);
        this.role = role;
    }
}
