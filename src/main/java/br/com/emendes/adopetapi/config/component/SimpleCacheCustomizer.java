package br.com.emendes.adopetapi.config.component;

import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.stereotype.Component;

import java.util.List;

import static br.com.emendes.adopetapi.util.ConstantUtil.USERS_CACHE_NAME;

@Component
public class SimpleCacheCustomizer implements CacheManagerCustomizer<ConcurrentMapCacheManager> {

  @Override
  public void customize(ConcurrentMapCacheManager cacheManager) {
    cacheManager.setCacheNames(List.of(USERS_CACHE_NAME));
  }

}
