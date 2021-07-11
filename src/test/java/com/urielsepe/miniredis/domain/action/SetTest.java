package com.urielsepe.miniredis.domain.action;

import com.urielsepe.miniredis.domain.infrastructure.InMemoryRepository;
import com.urielsepe.miniredis.domain.infrastructure.MockClock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public class SetTest {
    @Test
    void testSetWithAnEmptyKeyShouldFail() {
        Instant now = Instant.now();
        MockClock clock = new MockClock(now);
        InMemoryRepository repository = new InMemoryRepository(clock);
        Set set = new Set(repository);

        Assertions.assertThrows(IllegalArgumentException.class, () -> set.call("", "", null));
    }

    @Test
    void testSetWithANonEmptyKeyButWithoutAValueShouldFail() {
        Instant now = Instant.now();
        MockClock clock = new MockClock(now);
        InMemoryRepository repository = new InMemoryRepository(clock);
        Set set = new Set(repository);

        Assertions.assertThrows(IllegalArgumentException.class, () -> set.call("MY_KEY", "", null));
    }

    @Test
    void testSetWithANonEmptyKeyValueButWithAZeroExpirationSeconds() {
        Instant now = Instant.now();
        MockClock clock = new MockClock(now);
        InMemoryRepository repository = new InMemoryRepository(clock);
        Set set = new Set(repository);

        Assertions.assertThrows(IllegalArgumentException.class, () -> set.call("MY_KEY", "stored value", 0));
    }

    @Test
    void testSetWithValidKeyAndValueShouldSucceed() {
        Instant now = Instant.now();
        MockClock clock = new MockClock(now);
        InMemoryRepository repository = new InMemoryRepository(clock);
        Set set = new Set(repository);

        set.call("MY_KEY", "stored_value", null);

        Assertions.assertEquals( "stored_value", repository.get("MY_KEY"));
    }

    @Test
    void testSetWithValidKeyValueAndExpirationShouldSucceed() {
        Instant now = Instant.now();
        MockClock clock = new MockClock(now);
        InMemoryRepository repository = new InMemoryRepository(clock);
        Set set = new Set(repository);

        set.call("MY_KEY", "stored_value", 10);

        Assertions.assertEquals("stored_value", repository.get("MY_KEY"));
    }
}
