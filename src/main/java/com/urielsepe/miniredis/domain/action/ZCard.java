package com.urielsepe.miniredis.domain.action;

import com.urielsepe.miniredis.domain.exceptions.WrongTypeException;
import com.urielsepe.miniredis.domain.model.Repository;

import java.util.Objects;

/**
 * Returns the sorted set cardinality (number of elements) of the sorted set stored at key.
 */
public class ZCard {
    private final Repository repository;

    public ZCard(Repository repository) {
        this.repository = repository;
    }

    public int call(String key) {
        if (Objects.isNull(key) || key.isEmpty()) {
            throw new IllegalArgumentException("Key must not be empty");
        }

        try {
            return repository.zCard(key);
        } catch (IllegalArgumentException e) {
            throw new WrongTypeException();
        }
    }
}
