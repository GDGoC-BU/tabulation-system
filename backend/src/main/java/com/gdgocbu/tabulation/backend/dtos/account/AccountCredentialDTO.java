package com.gdgocbu.tabulation.backend.dtos.account;

import java.util.UUID;

public record AccountCredentialDTO(
        UUID id,
        String username,
        String passwordHash,
        String accountType
) {
}
