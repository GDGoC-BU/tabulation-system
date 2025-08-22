package com.michaelcanonizado.backend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Manager extends Account {
    @Column(nullable = false)
    private ManagerRole role;

    public Manager(String username, String passwordHash, ManagerRole role) {
        super(username, passwordHash);
        this.role = role;
    }
}
