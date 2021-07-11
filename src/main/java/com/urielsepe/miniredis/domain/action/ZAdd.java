package com.urielsepe.miniredis.domain.action;

import com.urielsepe.miniredis.domain.exceptions.WrongTypeException;
import com.urielsepe.miniredis.domain.model.Repository;

import java.util.Objects;

/**
 * Adds the specified member with the specified score to the sorted set stored at key.
 *
 * If a specified member is already a member of the sorted set,
 * the score is updated and the element reinserted at the right position to ensure the correct ordering.
 *
 * If key does not exist, a new sorted set with the specified members as sole members is created, like if the sorted
 * set was empty. If the key exists but does not hold a sorted set, an error is returned.
 *
 * The score values should be the string representation of a double precision floating point number. +inf and -inf
 * values are valid values as well.
 */
public class ZAdd {
    private final Repository repository;

    public ZAdd(Repository repository) {
        this.repository = repository;
    }

    public int call(String key, Double score, String member) {
        validateInput(key, score, member);
        try {
            return repository.zAdd(key, score, member);
        } catch (IllegalArgumentException e) {
            throw new WrongTypeException();
        }
    }

    private void validateInput(String key, Double score, String member) {
        if (Objects.isNull(key) || key.isEmpty()) {
            throw new IllegalArgumentException("Key must not be empty");
        }
        if (Objects.isNull(score)) {
            throw new IllegalArgumentException("Score must not be empty");
        }
        if (Objects.isNull(member) || member.isEmpty()) {
            throw new IllegalArgumentException("Value must not be empty");
        }
    }
}
