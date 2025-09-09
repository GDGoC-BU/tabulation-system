package com.michaelcanonizado.backend.services;

import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.customs.PageantAccessDeniedException;
import com.michaelcanonizado.backend.models.Pageant;
import com.michaelcanonizado.backend.repositories.PageantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PageantCacheService {
    @Autowired
    private PageantRepository repository;

    /*  TEMPORARY METHOD! PULL FROM CACHE.
        Extend a CacheService abstract class
        with redisTemplate definitions */
    public Pageant get(UUID id) {
        return repository
                .findAll()
                .stream()
                .findFirst().orElseThrow(() -> {
                    return new PageantAccessDeniedException(
                            "Pageant not found! Can't perform operation",
                            ErrorCode.PAGEANT_ACCESS_DENIED
                    );
                });
    }
}
