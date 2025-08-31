package com.michaelcanonizado.backend.aspects;

import com.michaelcanonizado.backend.annotations.RequirePageantStatus;
import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.entity.EntityNotFoundException;
import com.michaelcanonizado.backend.exceptions.entity.PageantStatusException;
import com.michaelcanonizado.backend.models.Pageant;
import com.michaelcanonizado.backend.models.PageantStatus;
import com.michaelcanonizado.backend.repositories.PageantRepository;
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

    @Before("@within(requirePageantStatus) || @annotation(requirePageantStatus)")
    public void checkPageantStatus(RequirePageantStatus requirePageantStatus) {
        Pageant pageant = repository.findSingleton().orElseThrow(() -> {
            return new EntityNotFoundException("A pageant doesn't exist! Create a new one.", ErrorCode.ENTITY_NOT_FOUND);
        });

        PageantStatus currentStatus = pageant.getStatus();

        boolean isAllowed = Arrays.asList(requirePageantStatus.value()).contains(currentStatus);
        if (isAllowed) {
            return;
        }

        String message = switch (currentStatus) {
            case PREPARATION -> "Pageant is locked! Wait for admin to open.";
            case FINALIZING -> "Pageant is finalizing! Operation not allowed!";
            case CLOSED -> "Pageant is closed! Operation not allowed!";
            default -> "Operation not allowed!";
        };

        throw new PageantStatusException(message, ErrorCode.PAGEANT_LOCKED);
    }
}
