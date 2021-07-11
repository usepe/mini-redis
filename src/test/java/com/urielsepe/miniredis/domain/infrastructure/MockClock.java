package com.urielsepe.miniredis.domain.infrastructure;

import com.urielsepe.miniredis.domain.model.Clock;

import java.time.Instant;

public class MockClock implements Clock {
    private final Instant now;

    public MockClock(Instant now) {
        this.now = now;
    }

    @Override
    public Instant currentTime() {
        return now;
    }
}
