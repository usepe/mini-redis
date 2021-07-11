package com.urielsepe.miniredis.domain.action;

import com.urielsepe.miniredis.domain.model.Repository;

import java.util.Objects;

/**
 * Get the value of key. If the key does not exist the special value nil is returned.
 * An error is returned if the value stored at key is not a string, because GET only handles string values.
 */
public class Get {
    private final Repository repository;

    public Get(Repository repository) {
        this.repository = repository;
    }

    public String call(String key) {
        if (Objects.isNull(key) || key.isEmpty()) {
            throw new IllegalArgumentException("Key must not be empty");
        }
        return repository.get(key);
    }
}
