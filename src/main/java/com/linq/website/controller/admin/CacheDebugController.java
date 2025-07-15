package com.linq.website.controller.admin;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cache")
public class CacheDebugController {

    private final CacheManager cacheManager;

    // Constructor injection
    public CacheDebugController(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @GetMapping("/check/{slug}")
    public String checkCache(@PathVariable String slug) {
        Cache cache = cacheManager.getCache("dynamicPageData");
        if (cache == null) {
            return "CACHE_NOT_FOUND";
        }

        Cache.ValueWrapper wrapper = cache.get(slug);
        if (wrapper == null) {
            return "EVICTED";
        }

        return "EXISTS: " + wrapper.get().getClass().getSimpleName();
    }
}