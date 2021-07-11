package com.urielsepe.miniredis.domain.action;

import com.urielsepe.miniredis.domain.exceptions.WrongTypeException;
import com.urielsepe.miniredis.domain.infrastructure.InMemoryRepository;
import com.urielsepe.miniredis.domain.infrastructure.MockClock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;

public class ZAddTest {
    @Test
    void testZAddShouldFailIfAnEmptyKeyIsPassedIn() {
        InMemoryRepository repository = new InMemoryRepository(new MockClock(Instant.now()));
        ZAdd zAdd = new ZAdd(repository);

        Assertions.assertThrows(IllegalArgumentException.class, () -> zAdd.call("", null, null));
    }

    @Test
    void testZAddShouldFailIfANullKeyIsPassedIn() {
        InMemoryRepository repository = new InMemoryRepository(new MockClock(Instant.now()));
        ZAdd zAdd = new ZAdd(repository);

        Assertions.assertThrows(IllegalArgumentException.class, () -> zAdd.call(null, null, null));
    }

    @Test
    void testZAddShouldFailIfANullScoreIsPassedIn() {
        InMemoryRepository repository = new InMemoryRepository(new MockClock(Instant.now()));
        ZAdd zAdd = new ZAdd(repository);

        Assertions.assertThrows(IllegalArgumentException.class, () -> zAdd.call("VALID_KEY", null, null));
    }

    @Test
    void testZAddShouldFailIfANullMemberIsPassedIn() {
        InMemoryRepository repository = new InMemoryRepository(new MockClock(Instant.now()));
        ZAdd zAdd = new ZAdd(repository);

        Assertions.assertThrows(IllegalArgumentException.class, () -> zAdd.call("VALID_KEY", 1.0, null));
    }

    @Test
    void testZAddShouldFailIfAnEmptyMemberIsPassedIn() {
        InMemoryRepository repository = new InMemoryRepository(new MockClock(Instant.now()));
        ZAdd zAdd = new ZAdd(repository);

        Assertions.assertThrows(IllegalArgumentException.class, () -> zAdd.call("VALID_KEY", 1.0, ""));
    }

    @Test
    void testZAddShouldGiveAnErrorIfTheKeyIsSetToSomethingThatIsNotASortedSet() {
        InMemoryRepository repository = new InMemoryRepository(new MockClock(Instant.now()));
        ZAdd zAdd = new ZAdd(repository);

        repository.set("MY_KEY", "im not a sorted set", null);

        Assertions.assertThrows(WrongTypeException.class, () -> zAdd.call("MY_KEY", 1.0, "one"));
    }

    @Test
    void testZAddShouldAddTheMemberWithTheScore() {
        InMemoryRepository repository = new InMemoryRepository(new MockClock(Instant.now()));
        ZAdd zAdd = new ZAdd(repository);

        Assertions.assertEquals(1, zAdd.call("MY_KEY", 1.0, "one"));
        Assertions.assertEquals(1, repository.zCard("MY_KEY"));
    }

    @Test
    void testZAddShouldAddANewMemberToTheSameSet() {
        InMemoryRepository repository = new InMemoryRepository(new MockClock(Instant.now()));
        ZAdd zAdd = new ZAdd(repository);

        repository.zAdd("MY_KEY", 1.0, "one");

        Assertions.assertEquals(1, zAdd.call("MY_KEY", 2.0, "two"));
        Assertions.assertEquals(2, repository.zCard("MY_KEY"));
    }
}
