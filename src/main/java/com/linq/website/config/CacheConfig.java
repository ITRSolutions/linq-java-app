package com.linq.website.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        List<String> cacheNamesList = List.of("pageMetaData", "companyMetaData",
                "footerBlocks", "navigation", "principalInvestigatorsNavigation",
                "meetOurPrincipalInvestigators", "faqAllQuestions");

        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        // Declare all cache names you'll use
        cacheManager.setCacheNames(cacheNamesList);
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(12, TimeUnit.HOURS)
                .maximumSize(1000));
        return cacheManager;
    }
}
