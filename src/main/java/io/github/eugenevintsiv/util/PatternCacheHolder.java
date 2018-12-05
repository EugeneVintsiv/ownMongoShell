package io.github.eugenevintsiv.util;

import com.google.common.base.Charsets;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.hash.Hashing;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Component
public class PatternCacheHolder {

    private Cache<String, Pattern> patternCache = CacheBuilder.newBuilder()
            .concurrencyLevel(4)
            .expireAfterWrite(15, TimeUnit.MINUTES)
            .build();

    public Pattern getPattern(String regexp) {
        final String key = buildKey(regexp);
        final Pattern pattern = patternCache.getIfPresent(key);
        if (pattern != null) {
            return pattern;
        } else {
            Pattern patternCompiled = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE);
            patternCache.put(key, patternCompiled);
            return patternCompiled;
        }
    }

    private String buildKey(String regexp) {
        return Hashing.goodFastHash(128).hashString(regexp, Charsets.UTF_8).toString();
    }
}
