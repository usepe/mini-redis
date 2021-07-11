package com.urielsepe.miniredis.domain.action;

import com.urielsepe.miniredis.domain.exceptions.WrongTypeException;
import com.urielsepe.miniredis.domain.infrastructure.InMemoryRepository;
import com.urielsepe.miniredis.domain.infrastructure.MockClock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IncrTest {
    @Test
    public void testIncrShouldFailWhenTryingToPerformAnIncrementOnAStringThatCannotBeConverted() {
        InMemoryRepository repository = new InMemoryRepository(new MockClock(Instant.now()));
        Incr incr = new Incr(repository);

        repository.set("MY_KEY", "some value that cannot be converted", null);

        Assertions.assertThrows(WrongTypeException.class, () -> incr.call("MY_KEY"));
        Assertions.assertSame("some value that cannot be converted", repository.get("MY_KEY"));
    }

    @Test
    public void testIncrShouldReturnOneWhenTheOperationSucceedsOnAKeyThatWasNotPreviouslySet() {
        InMemoryRepository repository = new InMemoryRepository(new MockClock(Instant.now()));
        Incr incr = new Incr(repository);

        Assertions.assertSame(1, incr.call("MY_KEY"));
        Assertions.assertEquals("1", repository.get("MY_KEY"));
    }

    @Test
    public void testIncrShouldUpdateAKeyThatCanBeConvertedToInteger() {
        InMemoryRepository repository = new InMemoryRepository(new MockClock(Instant.now()));
        Incr incr = new Incr(repository);

        repository.set("MY_KEY", "10", null);

        Assertions.assertSame(11, incr.call("MY_KEY"));
        Assertions.assertEquals("11", repository.get("MY_KEY"));
    }

    @Test
    public void testIncrSeveralTimesAValue() {
        InMemoryRepository repository = new InMemoryRepository(new MockClock(Instant.now()));
        Incr incr = new Incr(repository);

        int count = 10;
        for (int i = 0; i < count; i++) {
            incr.call("a");
        }

        Assertions.assertEquals(11, incr.call("a"));
    }

    @Test
    public void testIncrWithConcurrency() throws InterruptedException {
        InMemoryRepository repository = new InMemoryRepository(new MockClock(Instant.now()));
        Incr incr = new Incr(repository);

        int numberOfThreads = 100;
        ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        for (int i = 0; i < numberOfThreads; i++) {
            service.submit(() -> {
                incr.call("a");
                latch.countDown();
            });
        }
        latch.await();

        Assertions.assertEquals(101, incr.call("a"));
    }
}
