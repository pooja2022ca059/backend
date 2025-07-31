package com.aipm.ai_project_management.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {
    // Cache configuration is handled by application.properties
    // This class just enables caching
}