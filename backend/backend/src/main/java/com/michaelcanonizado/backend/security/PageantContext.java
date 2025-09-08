package com.michaelcanonizado.backend.security;

import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.customs.EntityNotFoundException;
import com.michaelcanonizado.backend.models.Pageant;
import com.michaelcanonizado.backend.repositories.PageantRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.UUID;

@Getter
@RequestScope
@Component
public class PageantContext {
    private final UUID id;

    @Autowired
    public PageantContext(PageantRepository repository) {
        /* TEMPORARY HACK!
           Get the pageant id from the jwt claim.
           Also add a special custom exception for
           this as this is simply not an entity search. */
        this.id = repository
                .findAll()
                .stream()
                .findFirst()
                .map(Pageant::getId)
                .orElseThrow(() -> {
                    return new EntityNotFoundException("Pageant not found!", ErrorCode.ENTITY_NOT_FOUND);
                });
    }
}
