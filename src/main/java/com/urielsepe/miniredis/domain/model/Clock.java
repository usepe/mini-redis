package com.urielsepe.miniredis.domain.model;

import java.time.Instant;

public interface Clock {
    Instant currentTime();
}
