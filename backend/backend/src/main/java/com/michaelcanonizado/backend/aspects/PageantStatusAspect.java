package com.michaelcanonizado.backend.aspects;

import com.michaelcanonizado.backend.annotations.RequirePageantStatus;
import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.customs.PageantStatusException;
import com.michaelcanonizado.backend.models.PageantStatus;
import com.michaelcanonizado.backend.repositories.PageantRepository;
import com.michaelcanonizado.backend.contexts.PageantContext;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class PageantStatusAspect {
    @Autowired
    private PageantRepository repository;

    @Autowired
    private PageantContext pageantContext;

    @Before("@within(requirePageantStatus) || @annotation(requirePageantStatus)")
    public void checkPageantStatus(RequirePageantStatus requirePageantStatus) {
        /* Get selected pageant status */
        PageantStatus currentStatus = pageantContext.getStatus();

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
