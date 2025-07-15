package com.linq.website.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
public class SlugCacheEvict {

    private static final Logger logger = LoggerFactory.getLogger(DynamicPageService.class);

    @CacheEvict(value = "companyMetaData", key = "'companyMetaData'")
    public void evictPageMetaDataCache() {
        logger.info("Evicted cache for companyMetaData");
    }

    // For specific page eviction
    @CacheEvict(value = "dynamicPageData", key = "#slug")
    public void evictPageCache(String slug) {
        logger.info("Evicted cache for slug: {}", slug);
    }

    @CacheEvict(value = "dynamicPageData", allEntries = true)
    public void evictAllPageCaches() {
        logger.info("Evicted entire dynamic page cache");
    }
}
