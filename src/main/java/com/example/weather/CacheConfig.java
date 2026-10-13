package com.example.weather;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/** Simple in-memory cache so repeated lookups don't hit the upstream API. */
@Configuration
@EnableCaching
public class CacheConfig {}
