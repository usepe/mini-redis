package com.urielsepe.miniredis.domain.action;

import com.urielsepe.miniredis.domain.infrastructure.InMemoryRepository;
import com.urielsepe.miniredis.domain.infrastructure.MockClock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Random;

public class DbSizeTest {
    @Test
    public void testDbSizeShouldReturnZeroOnAnEmptyDatabase() {
        MockClock clock = new MockClock(Instant.now());
        InMemoryRepository repository = new InMemoryRepository(clock);
        DbSize dbSize = new DbSize(repository);

        Assertions.assertEquals(0, dbSize.call());
    }

    @Test
    public void testDbSizeShouldAccountForExpiredKeysOnTheSizeCount() {
        MockClock clock = new MockClock(Instant.now().plusSeconds(15));
        InMemoryRepository repository = new InMemoryRepository(clock);
        DbSize dbSize = new DbSize(repository);

        repository.set("MY_KEY", "stored value", 10);

        Assertions.assertEquals(0, dbSize.call());
    }

    @Test
    public void testDbSizeShouldReturnTheExpectedQuantityOfKeys() {
        MockClock clock = new MockClock(Instant.now());
        InMemoryRepository repository = new InMemoryRepository(clock);
        DbSize dbSize = new DbSize(repository);

        Random random = new Random();
        int qty = random.nextInt(100);

        for (int i = 0; i < qty; i++) {
            repository.set("MY_KEY_" + i, "stored value", null);
        }

        Assertions.assertEquals(qty, dbSize.call());
    }
}
