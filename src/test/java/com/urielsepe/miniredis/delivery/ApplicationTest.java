package com.urielsepe.miniredis.delivery;

import com.urielsepe.miniredis.domain.infrastructure.InMemoryRepository;
import com.urielsepe.miniredis.domain.infrastructure.MockClock;
import com.urielsepe.miniredis.domain.model.Clock;
import com.urielsepe.miniredis.domain.model.Repository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Instant;

public class ApplicationTest {
    private Application app;
    private Repository repository;

    @BeforeEach
    public void beforeEach() {
        Clock clock = new MockClock(Instant.now());
        repository = new InMemoryRepository(clock);
        app = new Application(repository);
    }

    @Test
    public void testUnknownCommandEntered() {
        Assertions.assertEquals("unknown command 'nonexistent'", app.processInput("nonexistent"));
    }

    @Test
    public void testDbSizeWithEmptyDatabase() {
        Assertions.assertEquals("(integer) 0", app.processInput("dbsize"));
    }

    @Test
    public void testDbSizeWithNonEmptyDatabase() {
        repository.set("a", "value", null);
        repository.set("b", "value", null);
        repository.set("c", "value", null);

        Assertions.assertEquals("(integer) 3", app.processInput("dbsize"));
    }

    @Test
    public void testDbSizeWithWrongNumberOfArguments() {
        String expected = "wrong number of arguments for 'dbsize' command";
        Assertions.assertEquals(expected, app.processInput("dbsize something"));
    }

    @Test
    public void testDelOnEmptyKey() {
        Assertions.assertEquals("(integer) 0", app.processInput("del key"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"del", "del a b"})
    public void testDelWithWrongNumberOfArguments(String command) {
        Assertions.assertEquals("wrong number of arguments for 'del' command", app.processInput(command));
    }

    @Test
    public void testGetWithEmptyKey() {
        Assertions.assertEquals("(nil)", app.processInput("get key"));
    }

    @Test
    public void testGetWithNonEmptyKey() {
        repository.set("key", "some", null);
        Assertions.assertEquals("\"some\"", app.processInput("get key"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"get", "get a b"})
    public void testGetWithWrongNumberOfArguments(String command) {
        Assertions.assertEquals("wrong number of arguments for 'get' command", app.processInput(command));
    }

    @Test
    public void testIncrWithNonEmptyKeyContainingSomethingThatIsNotANumber() {
        repository.set("a", "some", null);
        Assertions.assertEquals("value is not an integer", app.processInput("incr a"));
    }

    @Test
    public void testIncrWithNonEmptyKeyContainingANumber() {
        repository.set("a", "10", null);
        Assertions.assertEquals("(integer) 11", app.processInput("incr a"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"incr", "incr a a"})
    public void testIncrWithWrongNumberOfArguments(String command) {
        Assertions.assertEquals("wrong number of arguments for 'incr' command", app.processInput(command));
    }

    @Test
    public void testSetCommandWithAKeyThatIsEmpty() {
        Assertions.assertEquals("OK", app.processInput("set a b"));
    }

    @Test
    public void testSetCommandWithAKeyThatIsNotEmpty() {
        repository.set("a", "c", null);
        Assertions.assertEquals("OK", app.processInput("set a b"));
    }

    @Test
    public void testSetCommandWithAKeyThatIsNotEmptyAndIsASet() {
        repository.zAdd("a", 1.0, "b");
        Assertions.assertEquals("OK", app.processInput("set a b"));
    }

    @Test
    public void testSetCommandWithExpiration() {
        Assertions.assertEquals("OK", app.processInput("set a b ex 10"));
    }

    @Test
    public void testSetCommandWithExpirationThatIsNotANumber() {
        Assertions.assertEquals("value is not an integer", app.processInput("set a b ex a"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"set", "set a", "set a b ex", "set a b c", "set a b ex 10 c"})
    public void testSetCommandWithWrongNumberOfArguments(String command) {
        Assertions.assertEquals("wrong number of arguments for 'set' command", app.processInput(command));
    }

    @ParameterizedTest
    @ValueSource(strings = {"zadd", "zadd a", "zadd a b c d"})
    public void testZAddCommandWithWrongNumberOfArguments(String command) {
        Assertions.assertEquals("wrong number of arguments for 'zadd' command", app.processInput(command));
    }

    @Test
    public void testZAddCommandWithAnInvalidScore() {
        Assertions.assertEquals("value is not a valid float", app.processInput("zadd a b c"));
    }

    @Test
    public void testZAddCommandWithAnInvalidKey() {
        repository.set("a", "b", null);
        Assertions.assertEquals("operation against a key holding the wrong kind of value", app.processInput("zadd a 1.0 c"));
    }

    @Test
    public void testZAddCommandThatIsAValid() {
        Assertions.assertEquals("(integer) 1", app.processInput("zadd a 1.0 b"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"zcard", "zcard a b"})
    public void testZCardCommandWithWrongNumberOfArguments(String command) {
        Assertions.assertEquals("wrong number of arguments for 'zcard' command", app.processInput(command));
    }

    @Test
    public void testZCardCommandWithAnEmptyKey() {
        Assertions.assertEquals("(integer) 0", app.processInput("zcard a"));
    }

    @Test
    public void testZCardCommandWithAWrongTypeOfKey() {
        repository.set("a", "b", null);
        Assertions.assertEquals("operation against a key holding the wrong kind of value", app.processInput("zcard a"));
    }

    @Test
    public void testZCardCommandWithAKeyHoldingASet() {
        repository.zAdd("a", 1.0, "b");
        repository.zAdd("a", 1.0, "b");
        Assertions.assertEquals("(integer) 1", app.processInput("zcard a"));
    }

    @Test
    public void testZCardCommandWithAKeyHoldingASetWithDifferentValues() {
        repository.zAdd("a", 1.0, "b");
        repository.zAdd("a", 1.0, "c");
        Assertions.assertEquals("(integer) 2", app.processInput("zcard a"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"zrange", "zrange a", "zrange a b"})
    public void testZRangeCommandWithWrongNumberOfArguments(String command) {
        Assertions.assertEquals("wrong number of arguments for 'zrange' command", app.processInput(command));
    }

    @Test
    public void testZRangeCommandWithAKeyThatIsNotASet() {
        repository.set("a", "b", null);
        String expected = "operation against a key holding the wrong kind of value";
        Assertions.assertEquals(expected, app.processInput("zrange a 0 1"));
    }

    @Test
    public void testZRangeCommandWithAKeyThatHoldsASet() {
        repository.zAdd("a", 1.0, "one");
        repository.zAdd("a", 2.0, "two");
        repository.zAdd("a", 3.0, "three");

        Assertions.assertEquals("1) \"one\"\n", app.processInput("zrange a 0 0"));
        Assertions.assertEquals("1) \"two\"\n", app.processInput("zrange a 1 1"));
        Assertions.assertEquals("1) \"three\"\n", app.processInput("zrange a 2 2"));

        Assertions.assertEquals("1) \"one\"\n", app.processInput("zrange a -3 -3"));
        Assertions.assertEquals("1) \"two\"\n", app.processInput("zrange a -2 -2"));
        Assertions.assertEquals("1) \"three\"\n", app.processInput("zrange a -1 -1"));

        Assertions.assertEquals("1) \"one\"\n2) \"two\"\n", app.processInput("zrange a 0 1"));
        Assertions.assertEquals("1) \"two\"\n2) \"three\"\n", app.processInput("zrange a -2 -1"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"zrank", "zrank a", "zrank a b c"})
    public void testZRankCommandWithWrongNumberOfArguments(String command) {
        Assertions.assertEquals("wrong number of arguments for 'zrank' command", app.processInput(command));
    }

    @Test
    public void testZRankCommandAgainstAKeyNotHoldingASet() {
        repository.set("a", "b", null);
        Assertions.assertEquals("operation against a key holding the wrong kind of value", app.processInput("zrank a b"));
    }

    @Test
    public void testZRankCommandAgaintsAKeyHoldingASet() {
        repository.zAdd("a", 1.0, "b");
        Assertions.assertEquals("(integer) 0", app.processInput("zrank a b"));
    }

    @Test
    public void testZRankCommandAgainstAnEmptyKey() {
        Assertions.assertEquals("(nil)", app.processInput("zrank a b"));
    }

    @Test
    public void testZRankCommandAgainstAKeyHoldingASetButWithAMemberThatIsNotPart() {
        repository.zAdd("a", 1.0, "b");
        Assertions.assertEquals("(nil)", app.processInput("zrank a c"));
    }
}
