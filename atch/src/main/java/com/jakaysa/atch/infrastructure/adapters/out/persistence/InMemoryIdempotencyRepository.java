package com.jakaysa.atch.infrastructure.adapters.out.persistence;

import com.jakaysa.atch.application.ports.out.IdempotencyRepository;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryIdempotencyRepository implements IdempotencyRepository {

    private final ConcurrentHashMap<String, Boolean> processedKeys = new ConcurrentHashMap<>();

    @Override
    public boolean wasProcessed(String key) {
        return processedKeys.containsKey(key);
    }

    @Override
    public void markAsProcessed(String key) {
        processedKeys.put(key, true);
    }

    @Override
    public void clear() {
        processedKeys.clear();
    }
}
