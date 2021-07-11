package com.urielsepe.miniredis.domain.action;

import com.urielsepe.miniredis.domain.model.Repository;

/**
 * Removes the specified keys. A key is ignored if it does not exist.
 */
public class Del {
    private final Repository repository;

    public Del(Repository repository) {
        this.repository = repository;
    }

    public int call(String ...keys) {
        return repository.del(keys);
    }
}
