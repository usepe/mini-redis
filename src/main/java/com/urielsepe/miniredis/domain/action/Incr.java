package com.urielsepe.miniredis.domain.action;

import com.urielsepe.miniredis.domain.exceptions.WrongTypeException;
import com.urielsepe.miniredis.domain.model.Repository;

/**
 * Increments the number stored at key by one. If the key does not exist, it is set to 0 before performing the operation.
 * An error is returned if the key contains a value of the wrong type or contains a string that can not be represented
 * as integer. This operation is limited to 64 bit signed integers.
 */
public class Incr {
    private final Repository repository;

    public Incr(Repository repository) {
        this.repository = repository;
    }

    public synchronized int call(String key) {
        try {
            return repository.incr(key);
        } catch (NumberFormatException e) {
            throw new WrongTypeException();
        }
    }
}
