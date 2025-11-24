package com.michaelcanonizado.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
public class CacheService {
    @Autowired
    private CacheManager cacheManager;

    public <T> T get(String cacheName, String key, Class<T> type) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache == null) return null;

        try {
            return cache.get(key, type);
        } catch (Exception e) {
            System.err.println("Error reading cached value: " + e.getMessage());
            return null;
        }
    }

    public void put(String cacheName, String key, Object value) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            try {
                cache.put(key, value);
            } catch (Exception e) {
                System.err.println("Failed to put " + key + " in cache!");
                System.err.println(e.getMessage());
            }
        }
    }

    public void evict(String cacheName, String key) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.evict(key);
        }
    }

    public void clear(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        }
    }
}
