package com.urielsepe.miniredis.domain.action;

import com.urielsepe.miniredis.domain.model.Repository;

/**
 * Return the number of keys in the currently-selected database.
 */
public class DbSize {
    private final Repository repository;

    public DbSize(Repository repository) {
        this.repository = repository;
    }

    public int call() {
        return repository.keyCount();
    }
}
