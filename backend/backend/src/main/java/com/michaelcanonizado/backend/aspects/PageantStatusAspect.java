package com.michaelcanonizado.backend.aspects;

import com.michaelcanonizado.backend.annotations.RequirePageantStatus;
import com.michaelcanonizado.backend.dtos.pageant.PageantContextDTO;
import com.michaelcanonizado.backend.dtos.pageant.PageantSummaryDTO;
import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.customs.PageantContextMissingException;
import com.michaelcanonizado.backend.exceptions.customs.PageantHeaderLookupFailureException;
import com.michaelcanonizado.backend.exceptions.customs.PageantStatusException;
import com.michaelcanonizado.backend.mappers.PageantMapper;
import com.michaelcanonizado.backend.models.Pageant;
import com.michaelcanonizado.backend.models.PageantStatus;
import com.michaelcanonizado.backend.repositories.PageantRepository;
import com.michaelcanonizado.backend.contexts.PageantContext;
import com.michaelcanonizado.backend.services.CacheService;
import com.michaelcanonizado.backend.utilities.CacheKeyBuilder;
import com.michaelcanonizado.backend.utilities.CacheNameConstants;
import com.michaelcanonizado.backend.utilities.RequestHeader;
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
    private PageantRepository pageantRepository;

    @Autowired
    private PageantContext pageantContext;

    @Autowired
    private RequestHeader requestHeader;

    @Autowired
    private PageantMapper pageantMapper;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private CacheKeyBuilder cacheKeyBuilder;

    @Before("@within(requirePageantStatus) || @annotation(requirePageantStatus)")
    public void checkPageantStatus(RequirePageantStatus requirePageantStatus) {
        /* Extract Pageant-Id from request headers and populate context
           so the service can extract the context of the selected
           pageant. */
        String headerKey = "Pageant-Id";
        String headerPageantId = requestHeader.getHeader(headerKey);

        /* No Pageant-Id attached in header */
        if (headerPageantId == null || headerPageantId.trim().isBlank()) {
            pageantContext.setPageant(null);
            throw new PageantContextMissingException(
                    "Entity locked under pageant status, but no Pageant-Id header is present!",
                    ErrorCode.PAGEANT_CONTEXT_MISSING
            );
        }

        try {
            UUID pageantId = UUID.fromString(headerPageantId);
            String CACHE_NAME = CacheNameConstants.TABULATION;
            String CACHE_KEY = cacheKeyBuilder.build("pageants", pageantId, "context");

            /* Check pageant in cache */
            PageantContextDTO pageantDTO = cacheService.get(
                    CACHE_NAME,
                    CACHE_KEY,
                    PageantContextDTO.class
            );
            if (pageantDTO == null) {
                /* If not in cache, check database */
                Pageant pageantInDatabase = pageantRepository.findById(pageantId).orElseThrow(() -> {
                    /* If it doesn't exist anywhere */
                    return new PageantHeaderLookupFailureException(
                            "Cannot resolve the attached '" + headerKey + "' header! Pageant doesn't exist.",
                            ErrorCode.INVALID_REQUEST_HEADER
                    );
                });
                pageantDTO = pageantMapper.toContextDTO(pageantInDatabase);
                /* Update cache */
                cacheService.put(
                        CACHE_NAME,
                        CACHE_KEY,
                        pageantDTO
                );
            }
            /* Store pageant in context */
            pageantContext.setPageant(pageantDTO);
        } catch (IllegalArgumentException e) {
            /* Pageant-Id is attached in header but invalid UUID format */
            pageantContext.setPageant(null);
            throw new PageantHeaderLookupFailureException(
                    "Cannot resolve the attached '" + headerKey + "' header! Invalid UUID format.",
                    ErrorCode.INVALID_REQUEST_HEADER
            );
        }

        /* Get selected pageant status */
        PageantStatus currentStatus = pageantContext.getStatus();

        /* Check if current pageant status aligns with
           the required status to run the method */
        boolean isAllowed = Arrays
                .asList(requirePageantStatus.value())
                .contains(currentStatus);
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
