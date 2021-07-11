package com.urielsepe.miniredis.domain.infrastructure;

import java.time.Instant;
import java.util.Objects;

public class ValueRepositoryModel {
    private String value;
    private Integer expirationSeconds;
    private Instant createdAt;

    public ValueRepositoryModel(String value, Integer expirationSeconds) {
        this.value = value;
        this.expirationSeconds = expirationSeconds;
        this.createdAt = Instant.now();
    }

    public String getValue() {
        return this.value;
    }

    public boolean isExpired(Instant currentTime) {
        if (Objects.isNull(expirationSeconds)) {
            return false;
        }
        return createdAt.plusSeconds(expirationSeconds).isBefore(currentTime);
    }

    public void setValue(String value) {
        this.value = value;
    }
}
