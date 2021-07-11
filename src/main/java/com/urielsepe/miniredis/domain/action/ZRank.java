package com.urielsepe.miniredis.domain.action;

import com.urielsepe.miniredis.domain.exceptions.WrongTypeException;
import com.urielsepe.miniredis.domain.model.Repository;

import java.util.Objects;

/**
 * Returns the rank of member in the sorted set stored at key, with the scores ordered from low to high.
 * The rank (or index) is 0-based, which means that the member with the lowest score has rank 0.
 */
public class ZRank {
    private final Repository repository;

    public ZRank(Repository repository) {
        this.repository = repository;
    }

    public Integer call(String key, String member) {
        validateInput(key, member);

        try {
            return repository.zRank(key, member);
        } catch (IllegalArgumentException e) {
            throw new WrongTypeException();
        }
    }

    private void validateInput(String key, String member) {
        if (Objects.isNull(key) || key.isEmpty()) {
            throw new IllegalArgumentException("Key must not be empty");
        }
        if (Objects.isNull(member) || member.isEmpty()) {
            throw new IllegalArgumentException("Member must not be empty");
        }
    }
}
