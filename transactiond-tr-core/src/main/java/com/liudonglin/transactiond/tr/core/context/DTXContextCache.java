package com.liudonglin.transactiond.tr.core.context;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DTXContextCache {

    private Map<String, Map<String, Object>> cache = new ConcurrentHashMap<>(64);

    private Map<String, Object> singlePropCache = new ConcurrentHashMap<>(64);

    public void attach(String mainKey, String key, Object attachment) {
        Objects.requireNonNull(mainKey);
        Objects.requireNonNull(key);
        Objects.requireNonNull(attachment);

        if (cache.containsKey(mainKey)) {
            Map<String, Object> map = cache.get(mainKey);
            map.put(key, attachment);
            return;
        }

        Map<String, Object> map = new HashMap<>();
        map.put(key, attachment);
        cache.put(mainKey, map);
    }

    public void attach(String key, Object attachment) {
        this.singlePropCache.put(key, attachment);
    }

    @SuppressWarnings("unchecked")
    public <T> T attachment(String mainKey, String key) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(mainKey);

        if (cache.containsKey(mainKey)) {
            if (cache.get(mainKey).containsKey(key)) {
                return (T) cache.get(mainKey).get(key);
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T> T attachment(String key) {
        return (T) this.singlePropCache.get(key);
    }

    public void remove(String mainKey, String key) {
        if (cache.containsKey(mainKey)) {
            cache.get(mainKey).remove(key);
        }
    }

    public void removeAll(String mainKey) {
        this.cache.remove(mainKey);
    }

    public boolean containsKey(String mainKey, String key) {
        return cache.containsKey(mainKey) && cache.get(mainKey).containsKey(key);
    }

    public boolean containsKey(String key) {
        return singlePropCache.containsKey(key);
    }

    public void remove(String key) {
        this.singlePropCache.remove(key);
    }

}
