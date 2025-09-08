package com.michaelcanonizado.backend.security;

import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.customs.PageantAccessDeniedException;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PageantGuard {
    public void assertAccess(UUID entityPageantId, UUID pageantContextId) {
        if (!pageantContextId.equals(entityPageantId)) {
            throw new PageantAccessDeniedException(
                    "Trying to access entity/ies that don't belong to the current pageant!",
                    ErrorCode.PAGEANT_ACCESS_DENIED
            );
        }
    }
}
