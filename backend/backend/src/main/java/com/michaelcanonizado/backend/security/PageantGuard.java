package com.michaelcanonizado.backend.security;

import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.customs.PageantAccessDeniedException;

import java.util.UUID;

public class PageantGuard {
    public static void assertAccess(UUID entityPageantId, UUID pageantContextId) {
        if (!pageantContextId.equals(entityPageantId)) {
            throw new PageantAccessDeniedException(
                    "Trying to access entity/ies that don't belong to the current pageant!",
                    ErrorCode.PAGEANT_ACCESS_DENIED
            );
        }
    }
}
