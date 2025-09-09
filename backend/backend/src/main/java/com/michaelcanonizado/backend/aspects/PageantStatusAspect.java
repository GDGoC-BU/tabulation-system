package com.michaelcanonizado.backend.aspects;

import com.michaelcanonizado.backend.annotations.RequirePageantStatus;
import com.michaelcanonizado.backend.services.PageantCacheService;
import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.customs.PageantAccessDeniedException;
import com.michaelcanonizado.backend.exceptions.customs.PageantStatusException;
import com.michaelcanonizado.backend.models.Pageant;
import com.michaelcanonizado.backend.models.PageantStatus;
import com.michaelcanonizado.backend.repositories.PageantRepository;
import com.michaelcanonizado.backend.security.PageantContext;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.UUID;

@Aspect
@Component
public class PageantStatusAspect {
    @Autowired
    private PageantRepository repository;

    @Autowired
    private PageantCacheService cacheService;

    @Autowired
    private PageantContext pageantContext;

    @Before("@within(requirePageantStatus) || @annotation(requirePageantStatus)")
    public void checkPageantStatus(RequirePageantStatus requirePageantStatus) {
        /* Get pageant id from JWT claim */
        UUID currentPageantId = pageantContext.getId();

        /* If admin just logged in and hasn't selected
           a pageant, deny access (Only pageant service
           methods are allowed to perform operations without
           selecting a pageant). */
        if (currentPageantId == null) {
            throw new PageantAccessDeniedException(
                    "Access denied! You haven't selected a pageant.",
                    ErrorCode.PAGEANT_ACCESS_DENIED);
        }

        /* Check pageant from cache */
        Pageant pageant = cacheService.get();
        /* If no pageant, check database */
        if (pageant == null) {
            pageant = repository.findById(currentPageantId).orElseThrow(() -> {
                return new PageantAccessDeniedException(
                        "Access denied! Pageant not found.",
                        ErrorCode.PAGEANT_ACCESS_DENIED
                );
            });
        }

        /* Extract pageant status */
        PageantStatus currentStatus = pageant.getStatus();

        /* Check if current pageant status aligns with
           the required status to run the method */
        boolean isAllowed = Arrays.asList(requirePageantStatus.value()).contains(currentStatus);
        if (isAllowed) {
            return;
        }

        /* Associate error message for each status */
        String message = switch (currentStatus) {
            case PREPARATION -> "Pageant is locked! Wait for admin to open.";
            case ONGOING -> "Pageant is ongoing! Can't perform operation.";
            case FINALIZING -> "Pageant is finalizing! Operation not allowed!";
            case CLOSED -> "Pageant is closed! Operation not allowed!";
            default -> "Operation not allowed!";
        };

        /* Throw error */
        throw new PageantStatusException(message, ErrorCode.PAGEANT_LOCKED);
    }
}
