package com.urielsepe.miniredis.domain.action;

import java.util.Objects;

public class Set {
    public boolean call(String key, String value, Integer expirationSeconds) {
        validateInput(key, value, expirationSeconds);
        return true;
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
