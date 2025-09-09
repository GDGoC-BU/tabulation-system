package com.michaelcanonizado.backend.contexts;

import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.customs.PageantAccessDeniedException;
import com.michaelcanonizado.backend.models.Pageant;
import com.michaelcanonizado.backend.models.PageantStatus;
import com.michaelcanonizado.backend.repositories.PageantRepository;
import com.michaelcanonizado.backend.services.PageantCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.UUID;

@RequestScope
@Component
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
       that the pageant is fully managed. */
    private final Pageant pageant;

    @Autowired
    public PageantContext(PageantRepository repository, PageantCacheService cacheService) {
        /* NOTE: This constructor runs on every request
           due to @RequestScope. */

        /* TEMPORARY HACK!
           Get the pageant id from the jwt claim.
           Also add a special custom exception for
           this as this is simply not an entity search. */

        /* Get pageant id from JWT claim */
        UUID pageantIdClaim = UUID.randomUUID();

        /* If there is no pageant id claim in the JWT,
           throw error. This means that the user is trying
           to access pageant details without choosing a
           pageant. */
        if (pageantIdClaim == null) {
            throw new PageantAccessDeniedException(
                    "Access denied! You haven't selected a pageant.",
                    ErrorCode.PAGEANT_ACCESS_DENIED);
        }

        /* Get pageant. cacheService will check cache,
           then database, else it will throw an error. */
        this.pageant = cacheService.get(pageantIdClaim);
    }

    public UUID getId() {
        return pageant.getId();
    }

    public PageantStatus getStatus() {
        return pageant.getStatus();
    }

    public void assertAccess(UUID entityPageantId) {
        if (!pageant.getId().equals(entityPageantId)) {
            throw new PageantAccessDeniedException(
                    "Trying to access entity/ies that don't belong to the current pageant!",
                    ErrorCode.PAGEANT_ACCESS_DENIED
            );
        }
    }
}
