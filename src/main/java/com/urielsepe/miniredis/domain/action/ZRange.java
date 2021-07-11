package com.urielsepe.miniredis.domain.action;

import com.urielsepe.miniredis.domain.exceptions.WrongTypeException;
import com.urielsepe.miniredis.domain.infrastructure.InMemoryRepository;
import com.urielsepe.miniredis.domain.model.Repository;

import java.util.List;
import java.util.Objects;

/**
 * Returns the specified range of elements in the sorted set stored at <key>.
 */
public class ZRange {
    private final Repository repository;

    public ZRange(Repository repository) {
        this.repository = repository;
    }

    public List<String> call(String key, Integer start, Integer stop) {
        validateInput(key, start, stop);

        try {
            return repository.zRange(key, start, stop);
        } catch (IllegalArgumentException e) {
            throw new WrongTypeException();
        }
    }

    private void validateInput(String key, Integer start, Integer stop) {
        if (Objects.isNull(key) || key.isEmpty()) {
            throw new IllegalArgumentException("Key must not be empty");
        }
        if (Objects.isNull(start)) {
            throw new IllegalArgumentException("Start must not be empty");
        }
        if (Objects.isNull(stop)) {
            throw new IllegalArgumentException("Stop must not be empty");
        }
    }
}
