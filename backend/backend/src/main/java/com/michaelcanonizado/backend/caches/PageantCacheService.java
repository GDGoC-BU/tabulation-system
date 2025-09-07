package com.michaelcanonizado.backend.caches;

import com.michaelcanonizado.backend.models.Pageant;
import com.michaelcanonizado.backend.repositories.PageantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PageantCacheService {
    @Autowired
    private PageantRepository repository;

    /*  TEMPORARY METHOD! PULL FROM CACHE.
        Extend a CacheService abstract class
        with redisTemplate definitions */
    public Pageant get() {
        return repository.findAll().getFirst();
    }
}
