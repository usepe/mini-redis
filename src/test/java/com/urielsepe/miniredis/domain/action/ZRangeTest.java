package com.urielsepe.miniredis.domain.action;

import com.urielsepe.miniredis.domain.exceptions.WrongTypeException;
import com.urielsepe.miniredis.domain.infrastructure.InMemoryRepository;
import com.urielsepe.miniredis.domain.infrastructure.MockClock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ZRangeTest {
    @Test
    public void testZRangeShouldFailIfANullKeyIsPassedIn() {
        InMemoryRepository repository = new InMemoryRepository(new MockClock(Instant.now()));
        ZRange zRange = new ZRange(repository);

        Assertions.assertThrows(IllegalArgumentException.class, () -> zRange.call(null, 0, 0));
    }

    @Test
    public void testZRangeShouldFailIfAnEmptyKeyIsPassedIn() {
        InMemoryRepository repository = new InMemoryRepository(new MockClock(Instant.now()));
        ZRange zRange = new ZRange(repository);

        Assertions.assertThrows(IllegalArgumentException.class, () -> zRange.call("", 0, 0));
    }

    @Test
    public void testZRangeShouldFailIfAKeyThatIsNotFromASetIsPassedIn() {
        InMemoryRepository repository = new InMemoryRepository(new MockClock(Instant.now()));
        ZRange zRange = new ZRange(repository);

        repository.set("MY_KEY", "some string value", null);

        Assertions.assertThrows(WrongTypeException.class, () -> zRange.call("MY_KEY", 0, 0));
    }

    @Test
    public void testZRangeShouldFailIfStartIsNull() {
        InMemoryRepository repository = new InMemoryRepository(new MockClock(Instant.now()));
        ZRange zRange = new ZRange(repository);

        Assertions.assertThrows(IllegalArgumentException.class, () -> zRange.call("MY_KEY", null, 0));
    }

    @Test
    public void testZRangeShouldFailIfStopIsNull() {
        InMemoryRepository repository = new InMemoryRepository(new MockClock(Instant.now()));
        ZRange zRange = new ZRange(repository);

        Assertions.assertThrows(IllegalArgumentException.class, () -> zRange.call("MY_KEY", 0, null));
    }

    @Test
    public void testZRangeShouldReturnAListWithOneElementIfBothStartAndEndAreZeroForASingleElementSet() {
        InMemoryRepository repository = new InMemoryRepository(new MockClock(Instant.now()));
        ZRange zRange = new ZRange(repository);

        repository.zAdd("MY_KEY", 1.0, "one");

        List<String> expected = Collections.singletonList("one");

        Assertions.assertEquals(expected, zRange.call("MY_KEY", 0, 0));
    }

    @Test
    public void testZRangeShouldReturnAListWithOneElementIfBothStartAndEndAreMinusOneForASingleElementSet() {
        InMemoryRepository repository = new InMemoryRepository(new MockClock(Instant.now()));
        ZRange zRange = new ZRange(repository);

        repository.zAdd("MY_KEY", 1.0, "one");

        List<String> expected = Collections.singletonList("one");

        Assertions.assertEquals(expected, zRange.call("MY_KEY", -1, -1));
    }

    @Test
    public void testZRangeShouldReturnAListWithOneElementIfBothStartAndEndAreMinusOneForAMultipleElementSet() {
        InMemoryRepository repository = new InMemoryRepository(new MockClock(Instant.now()));
        ZRange zRange = new ZRange(repository);

        repository.zAdd("MY_KEY", 1.0, "one");
        repository.zAdd("MY_KEY", 2.0, "two");
        repository.zAdd("MY_KEY", 3.0, "three");

        List<String> expected = Collections.singletonList("three");

        Assertions.assertEquals(expected, zRange.call("MY_KEY", -1, -1));
    }

    @Test
    public void testZRangeShouldReturnAListWithOneElementIfBothStartAndEndAreZeroForAMultipleElementSet() {
        InMemoryRepository repository = new InMemoryRepository(new MockClock(Instant.now()));
        ZRange zRange = new ZRange(repository);

        repository.zAdd("MY_KEY", 1.0, "one");
        repository.zAdd("MY_KEY", 2.0, "two");
        repository.zAdd("MY_KEY", 3.0, "three");

        List<String> expected = Collections.singletonList("one");

        Assertions.assertEquals(expected, zRange.call("MY_KEY", 0, 0));
    }

    @Test
    public void testZRangeShouldReturnAnEmptyListIfMinIsGreaterThanMax() {
        InMemoryRepository repository = new InMemoryRepository(new MockClock(Instant.now()));
        ZRange zRange = new ZRange(repository);

        repository.zAdd("MY_KEY", 1.0, "one");
        repository.zAdd("MY_KEY", 2.0, "two");
        repository.zAdd("MY_KEY", 3.0, "three");

        List<String> expected = Collections.emptyList();

        Assertions.assertEquals(expected, zRange.call("MY_KEY", 4, 0));
    }

    @Test
    public void testZRangeShouldReturnAnEmptyListIfMinIsGreaterThanMaxElementOfTheSet() {
        InMemoryRepository repository = new InMemoryRepository(new MockClock(Instant.now()));
        ZRange zRange = new ZRange(repository);

        repository.zAdd("MY_KEY", 1.0, "one");
        repository.zAdd("MY_KEY", 2.0, "two");
        repository.zAdd("MY_KEY", 3.0, "three");

        List<String> expected = Collections.emptyList();

        Assertions.assertEquals(expected, zRange.call("MY_KEY", 4, 5));
    }

    @Test
    public void testZRangeShouldReturnTheFullSetWhenMaxIsGreaterThanTheContents() {
        InMemoryRepository repository = new InMemoryRepository(new MockClock(Instant.now()));
        ZRange zRange = new ZRange(repository);

        repository.zAdd("MY_KEY", 1.0, "one");
        repository.zAdd("MY_KEY", 2.0, "two");
        repository.zAdd("MY_KEY", 3.0, "three");

        List<String> expected = new LinkedList<>();
        expected.add("one");
        expected.add("two");
        expected.add("three");

        Assertions.assertEquals(expected, zRange.call("MY_KEY", 0, 5));
    }

    @Test
    public void testZRangeShouldReturnTwoElementsWhenStartAtTheSecondMember() {
        InMemoryRepository repository = new InMemoryRepository(new MockClock(Instant.now()));
        ZRange zRange = new ZRange(repository);

        repository.zAdd("MY_KEY", 1.0, "one");
        repository.zAdd("MY_KEY", 2.0, "two");
        repository.zAdd("MY_KEY", 3.0, "three");

        List<String> expected = new LinkedList<>();
        expected.add("two");
        expected.add("three");

        Assertions.assertEquals(expected, zRange.call("MY_KEY", 1, 5));
    }

    @Test
    public void testZRangeShouldReturnOneElementWhenStartAtTheFirstToLastMember() {
        InMemoryRepository repository = new InMemoryRepository(new MockClock(Instant.now()));
        ZRange zRange = new ZRange(repository);

        repository.zAdd("MY_KEY", 1.0, "one");
        repository.zAdd("MY_KEY", 2.0, "two");
        repository.zAdd("MY_KEY", 3.0, "three");

        List<String> expected = new LinkedList<>();
        expected.add("three");

        Assertions.assertEquals(expected, zRange.call("MY_KEY", -1, 2));
    }

    @Test
    public void testZRangeShouldReturnTwoElementsWhenStartAtTheSecondToLastAndStopAtTheFirstToLast() {
        InMemoryRepository repository = new InMemoryRepository(new MockClock(Instant.now()));
        ZRange zRange = new ZRange(repository);

        repository.zAdd("MY_KEY", 1.0, "one");
        repository.zAdd("MY_KEY", 2.0, "two");
        repository.zAdd("MY_KEY", 3.0, "three");

        List<String> expected = new LinkedList<>();
        expected.add("two");
        expected.add("three");

        Assertions.assertEquals(expected, zRange.call("MY_KEY", -2, -1));
    }

    @Test
    public void testZRangeShouldReturnAnEmptyListIfTheStartIsTheFirstToLastAndTheStopIsSecondToLast() {
        InMemoryRepository repository = new InMemoryRepository(new MockClock(Instant.now()));
        ZRange zRange = new ZRange(repository);

        repository.zAdd("MY_KEY", 1.0, "one");
        repository.zAdd("MY_KEY", 2.0, "two");
        repository.zAdd("MY_KEY", 3.0, "three");

        List<String> expected = Collections.emptyList();

        Assertions.assertEquals(expected, zRange.call("MY_KEY", -1, -2));
    }
}
