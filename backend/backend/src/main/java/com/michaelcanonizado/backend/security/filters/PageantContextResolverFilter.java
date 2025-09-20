package com.michaelcanonizado.backend.security.filters;

import com.michaelcanonizado.backend.contexts.PageantContext;
import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.customs.PageantHeaderLookupFailureException;
import com.michaelcanonizado.backend.models.Pageant;
import com.michaelcanonizado.backend.repositories.PageantRepository;
import com.michaelcanonizado.backend.services.PageantCacheService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class PageantContextResolverFilter extends OncePerRequestFilter {
    @Autowired
    private PageantContext pageantContext;

    @Autowired
    private PageantCacheService pageantCacheService;

    @Autowired
    private PageantRepository pageantRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String headerKey = "Pageant-Id";
        String headerPageantId = request.getHeader(headerKey);

        if (headerPageantId != null  && !headerPageantId.isBlank()) {
            try {
                UUID pageantId = UUID.fromString(headerPageantId);

                /* Check cache */
                Pageant pageant = pageantCacheService.get(pageantId).orElseGet(() -> {
                    /* If not in cache, check database */
                    Pageant pageantInDatabase = pageantRepository.findById(pageantId).orElseThrow(() -> {
                        /* If it doesn't exist anywhere */
                        return new PageantHeaderLookupFailureException(
                                "Cannot resolve the attached '" + headerKey + "' header! Pageant doesn't exist.",
                                ErrorCode.INVALID_REQUEST_HEADER
                        );
                    });

                    /* Update pageant cache */
                    pageantCacheService.put(pageantInDatabase);

                    /* Return the found pageant */
                    return pageantInDatabase;
                });

                /* Store pageant in context */
                pageantContext.setSelectedPageant(pageant);
            } catch (IllegalArgumentException e) {
                /* Pageant-Id is attached in header but invalid UUID format */
                pageantContext.setSelectedPageant(null);
                throw new PageantHeaderLookupFailureException(
                        "Cannot resolve the attached '" + headerKey + "' header! Invalid UUID format.",
                        ErrorCode.INVALID_REQUEST_HEADER
                );
            }

        }
        /* No Pageant-Id attached in header */
        else {
            pageantContext.setSelectedPageant(null);
        }

        filterChain.doFilter(request, response);
    }
}
