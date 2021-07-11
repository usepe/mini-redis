package com.urielsepe.miniredis.domain.action;

import com.urielsepe.miniredis.domain.exceptions.WrongTypeException;
import com.urielsepe.miniredis.domain.infrastructure.InMemoryRepository;
import com.urielsepe.miniredis.domain.infrastructure.MockClock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;

public class ZRankTest {
    @Test
    public void testZRankShouldFailWhenANullKeyIsPassedIn() {
        InMemoryRepository repository = new InMemoryRepository(new MockClock(Instant.now()));
        ZRank zRank = new ZRank(repository);

        Assertions.assertThrows(IllegalArgumentException.class, () -> zRank.call(null, null));
    }

    @Test
    public void testZRankShouldFailWhenAnEmptyKeyIsPassedIn() {
        InMemoryRepository repository = new InMemoryRepository(new MockClock(Instant.now()));
        ZRank zRank = new ZRank(repository);

        Assertions.assertThrows(IllegalArgumentException.class, () -> zRank.call("", null));
    }

    @Test
    public void testZRankShouldFailWhenPassedInANullMember() {
        InMemoryRepository repository = new InMemoryRepository(new MockClock(Instant.now()));
        ZRank zRank = new ZRank(repository);

        Assertions.assertThrows(IllegalArgumentException.class, () -> zRank.call("MY_KEY", null));
    }

    @Test
    public void testZRankShouldFailWhenPassedInAnEmptyMember() {
        InMemoryRepository repository = new InMemoryRepository(new MockClock(Instant.now()));
        ZRank zRank = new ZRank(repository);

        Assertions.assertThrows(IllegalArgumentException.class, () -> zRank.call("MY_KEY", ""));
    }

    @Test
    public void testZRankShouldFailWhenThePassedInKeyIsNotForASet() {
        InMemoryRepository repository = new InMemoryRepository(new MockClock(Instant.now()));
        ZRank zRank = new ZRank(repository);

        repository.set("MY_KEY", "some value", null);

        Assertions.assertThrows(WrongTypeException.class, () -> zRank.call("MY_KEY", "one"));
    }

    @Test
    public void testZRankShouldReturnZeroWhenPassedInTheLowestMemberOfASet() {
        InMemoryRepository repository = new InMemoryRepository(new MockClock(Instant.now()));
        ZRank zRank = new ZRank(repository);

        repository.zAdd("MY_KEY", Double.NEGATIVE_INFINITY, "one");
        repository.zAdd("MY_KEY", Double.POSITIVE_INFINITY, "two");

        Assertions.assertEquals(0, zRank.call("MY_KEY", "one"));
    }

    @Test
    public void testZRankShouldReturnOneWhenPassedInTheHighestMemberOfASet() {
        InMemoryRepository repository = new InMemoryRepository(new MockClock(Instant.now()));
        ZRank zRank = new ZRank(repository);

        repository.zAdd("MY_KEY", Double.NEGATIVE_INFINITY, "one");
        repository.zAdd("MY_KEY", Double.POSITIVE_INFINITY, "two");

        Assertions.assertEquals(1, zRank.call("MY_KEY", "two"));
    }
}
