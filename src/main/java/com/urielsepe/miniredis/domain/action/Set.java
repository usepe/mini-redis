package com.urielsepe.miniredis.domain.action;

import com.urielsepe.miniredis.domain.model.Repository;

import java.util.Objects;

/**
 * Set key to hold the string value. If key already holds a value, it is overwritten, regardless of its type.
 * Any previous time to live associated with the key is discarded on successful SET operation.
 */
public class Set {
    private final Repository repository;

    public Set(Repository repository) {
        this.repository = repository;
    }

    public synchronized void call(String key, String value, Integer expirationSeconds) {
        validateInput(key, value, expirationSeconds);
        repository.set(key, value, expirationSeconds);
    }

    private void validateInput(String key, String value, Integer expirationSeconds) throws IllegalArgumentException {
        if (key.isEmpty()) {
            throw new IllegalArgumentException("KEY must not be empty");
        } else if (value.isEmpty()) {
            throw new IllegalArgumentException("VALUE must not be empty");
        } else if (Objects.nonNull(expirationSeconds) && expirationSeconds.equals(0)) {
            throw new IllegalArgumentException("EX must be greater than zero");
        }
    }
}
