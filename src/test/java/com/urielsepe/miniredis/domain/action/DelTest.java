package com.urielsepe.miniredis.domain.action;

import com.urielsepe.miniredis.domain.infrastructure.InMemoryRepository;
import com.urielsepe.miniredis.domain.infrastructure.MockClock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;

public class DelTest {
    @Test
    public void testDelShouldReturnZeroWhenThePassedInKeyDoesNotExists() {
        Instant now = Instant.now();
        MockClock clock = new MockClock(now);
        InMemoryRepository repository = new InMemoryRepository(clock);
        Del del = new Del(repository);

        Assertions.assertSame(del.call("NON_EXISTANT_KEY"), 0);
    }

    @Test
    public void testDelShouldReturnOneWhenThePassedInKeyDoesExists() {
        Instant now = Instant.now();
        MockClock clock = new MockClock(now);
        InMemoryRepository repository = new InMemoryRepository(clock);
        Del del = new Del(repository);

        repository.set("MY_KEY", "stored value", null);

        Assertions.assertSame(del.call("MY_KEY"), 1);
        Assertions.assertNull(repository.get("MY_KEY"));
    }

    @Test
    public void testDelShouldReturnZeroWhenThePassedInKeysDoesNotExists() {
        Instant now = Instant.now();
        MockClock clock = new MockClock(now);
        InMemoryRepository repository = new InMemoryRepository(clock);
        Del del = new Del(repository);

        Assertions.assertEquals(del.call("NON_EXISTANT_KEY", "NON_EXISTANT_KEY_2"), 0);
    }

    @Test void testDelShouldReturnOneWhenOneOfTwoKeysDoesExists() {
        Instant now = Instant.now();
        MockClock clock = new MockClock(now);
        InMemoryRepository repository = new InMemoryRepository(clock);
        Del del = new Del(repository);

        repository.set("MY_KEY", "stored value", null);

        Assertions.assertEquals(del.call("MY_KEY", "NON_EXISTANT_KEY_2"), 1);
        Assertions.assertNull(repository.get("MY_KEY"));
    }
}
