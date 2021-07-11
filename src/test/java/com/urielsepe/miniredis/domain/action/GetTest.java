package com.urielsepe.miniredis.domain.action;

import com.urielsepe.miniredis.domain.infrastructure.InMemoryRepository;
import com.urielsepe.miniredis.domain.infrastructure.MockClock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;

public class GetTest {

    @Test
    public void testGetShouldReturnNullWhenKeyDoesNotExists() {
        Instant now = Instant.now();
        MockClock clock = new MockClock(now);
        InMemoryRepository repository = new InMemoryRepository(clock);
        Get get = new Get(repository);

        Assertions.assertNull(get.call("MY_NON_EXISTANT_KEY"));
    }

    @Test
    public void testGetWithAnEmptyKeyShouldFail() {
        Instant now = Instant.now();
        MockClock clock = new MockClock(now);
        InMemoryRepository repository = new InMemoryRepository(clock);
        Get get = new Get(repository);

        Assertions.assertThrows(IllegalArgumentException.class, () -> get.call(""));
    }

    @Test
    public void testGetWithANullKeyShouldFail() {
        Instant now = Instant.now();
        MockClock clock = new MockClock(now);
        InMemoryRepository repository = new InMemoryRepository(clock);
        Get get = new Get(repository);

        Assertions.assertThrows(IllegalArgumentException.class, () -> get.call(null));
    }

    @Test
    public void testGetShouldReturnValueWhenKeyDoesExists() {
        Instant now = Instant.now();
        MockClock clock = new MockClock(now);
        InMemoryRepository repository = new InMemoryRepository(clock);
        Get get = new Get(repository);

        repository.set("MY_KEY", "stored value", null);

        Assertions.assertEquals("stored value", get.call("MY_KEY"));
    }

    @Test
    public void testGetShouldReturnNullWhenTheValueExpires() {
        Instant inFifteenSeconds = Instant.now().plusSeconds(15);
        MockClock clock = new MockClock(inFifteenSeconds);

        InMemoryRepository repository = new InMemoryRepository(clock);
        Get get = new Get(repository);

        repository.set("MY_KEY", "stored value", 10);

        Assertions.assertNull(get.call("MY_KEY"));
    }
}
