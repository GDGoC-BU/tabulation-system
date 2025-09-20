package com.michaelcanonizado.backend.services;

import com.michaelcanonizado.backend.models.Pageant;
import com.michaelcanonizado.backend.repositories.PageantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PageantCacheService {
    @Autowired
    private PageantRepository repository;

    /*  TEMPORARY FETCH! PULL FROM CACHE.
        Extend a CacheService abstract class
        with redisTemplate definitions */
    public Optional<Pageant> get(UUID id) {
        return repository.findById(id);
    }

    public void put(Pageant pageantInDatabase) {
        /* Make sure your PageantCacheService.put(...) is safe in concurrent requests. If multiple requests miss the cache simultaneously, you don’t want race conditions. Usually, a ConcurrentHashMap or Caffeine handles this. */
    }
}
