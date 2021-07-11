package com.urielsepe.miniredis.domain.infrastructure;

import com.urielsepe.miniredis.domain.model.Clock;

import java.time.Instant;

public class SystemClock implements Clock {
    @Override
    public Instant currentTime() {
        return Instant.now();
    }
}
