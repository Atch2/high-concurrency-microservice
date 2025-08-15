package com.jakaysa.atch.application.ports.out;


public interface IdempotencyRepository {
    

    boolean wasProcessed(String key);
    

    void markAsProcessed(String key);
    

    void clear();
}
