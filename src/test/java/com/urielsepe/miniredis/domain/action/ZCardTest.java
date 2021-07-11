package com.urielsepe.miniredis.domain.action;

import com.urielsepe.miniredis.domain.exceptions.WrongTypeException;
import com.urielsepe.miniredis.domain.infrastructure.InMemoryRepository;
import com.urielsepe.miniredis.domain.infrastructure.MockClock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Random;

public class ZCardTest {
    @Test
    public void testZCardShouldFailIfPassedInANullKey() {
        InMemoryRepository repository = new InMemoryRepository(new MockClock(Instant.now()));
        ZCard zCard = new ZCard(repository);

        Assertions.assertThrows(IllegalArgumentException.class, () -> zCard.call(null));
    }

    @Test
    public void testZCardShouldFailIfPassedInAnEmptyKey() {
        InMemoryRepository repository = new InMemoryRepository(new MockClock(Instant.now()));
        ZCard zCard = new ZCard(repository);

        Assertions.assertThrows(IllegalArgumentException.class, () -> zCard.call(""));
    }

    @Test
    public void testZCardShouldFailIfPassedInKeyThatStoresValues() {
        InMemoryRepository repository = new InMemoryRepository(new MockClock(Instant.now()));
        ZCard zCard = new ZCard(repository);

        repository.set("MY_KEY", "some value key", null);

        Assertions.assertThrows(WrongTypeException.class, () -> zCard.call("MY_KEY"));
    }

    @Test
    public void testZCardShouldReturnTheCorrectCardinalityForTheKey() {
        InMemoryRepository repository = new InMemoryRepository(new MockClock(Instant.now()));
        ZCard zCard = new ZCard(repository);

        Random random = new Random();
        int qty = random.nextInt(100);

        for (double i = 0; i < qty; i++) {
            repository.zAdd("MY_KEY", i, "this is the " + i + "member");
        }

        Assertions.assertEquals(qty, zCard.call("MY_KEY"));
    }

    @Test
    public void testZCardShouldReturnZeroIfTheKeyDoesNotExists() {
        InMemoryRepository repository = new InMemoryRepository(new MockClock(Instant.now()));
        ZCard zCard = new ZCard(repository);

        Assertions.assertEquals(0, zCard.call("MY_KEY"));
    }
}
