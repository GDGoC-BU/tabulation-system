package com.michaelcanonizado.backend.contexts;

import com.michaelcanonizado.backend.dtos.pageant.PageantStatusDTO;
import com.michaelcanonizado.backend.dtos.pageant.PageantSummaryDTO;
import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.customs.PageantAccessDeniedException;
import com.michaelcanonizado.backend.exceptions.customs.PageantContextMissingException;
import com.michaelcanonizado.backend.models.Pageant;
import com.michaelcanonizado.backend.models.PageantStatus;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Arrays;
import java.util.UUID;

@RequestScope
@Component
@Setter
public class PageantContext {
    /* Never expose the actual pageant object!
       If the object came from cache, that object
       is detached! This prevents attaching it to
       other entities and causing transient
       database errors.

       When you need to attach to an entity
       (E.g: Creating a Candidate and you want to
       relate it the selected Pageant), get the id
       from here and call the database. This ensures
       that the pageant is a fully managed entity. */
    private PageantSummaryDTO selectedPageant;

    public UUID getId() {
        if (selectedPageant == null) {
            throw new PageantContextMissingException(
                    "Cannot get pageant.id! No pageant is selected for this request or usage of PageantContext without @RequirePageantStatus",
                    ErrorCode.PAGEANT_CONTEXT_MISSING
            );
        }
        return selectedPageant.id();
    }

    public PageantStatus getStatus() {
        if (selectedPageant == null) {
            throw new PageantContextMissingException(
                    "Cannot get pageant.status! No pageant is selected for this request or usage of PageantContext without @RequirePageantStatus",
                    ErrorCode.PAGEANT_CONTEXT_MISSING
            );
        }
        PageantStatusDTO statusDTO = selectedPageant.status();
        return Arrays
                .stream(PageantStatus.values())
                .filter(status -> status.name().equals(statusDTO.value()))
                .findFirst()
                .orElseThrow(() -> {
                    return new IllegalArgumentException(
                            "No matching PageantStatus for value: " + statusDTO.value()
                    );
                });
    }

    public void assertAccess(UUID entityPageantId) {
        if (selectedPageant == null) {
            throw new PageantContextMissingException(
                    "Cannot assert access! No pageant is selected for this request.",
                    ErrorCode.PAGEANT_CONTEXT_MISSING
            );
        }
        if (!selectedPageant.id().equals(entityPageantId)) {
            throw new PageantAccessDeniedException(
                    "Trying to access entity/ies that don't belong to the current pageant!",
                    ErrorCode.PAGEANT_ACCESS_DENIED
            );
        }
    }
}
