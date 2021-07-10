package com.urielsepe.miniredis.domain.action;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Set key to hold the string value. If key already holds a value, it is overwritten, regardless of its type.
 * Any previous time to live associated with the key is discarded on successful SET operation.
 */
public class SetTest {
    @Test
    void testSetWithAnEmptyKeyShouldFail() {
        Set set = new Set();

        Assertions.assertThrows(IllegalArgumentException.class, () -> set.call("", "", null));
    }

    @Test
    void testSetWithANonEmptyKeyButWithoutAValueShouldFail() {
        Set set = new Set();

        Assertions.assertThrows(IllegalArgumentException.class, () -> set.call("MY_KEY", "", null));
    }

    @Test
    void testSetWithANonEmptyKeyValueButWithAZeroExpirationSeconds() {
        Set set = new Set();

        Assertions.assertThrows(IllegalArgumentException.class, () -> set.call("MY_KEY", "stored value", 0));
    }

    @Test
    void testSetWithValidKeyAndValueShouldSucceed() {
        Set set = new Set();

        Assertions.assertTrue(set.call("MY_KEY", "stored_value", null));
    }

    @Test
    void testSetWithValidKeyValueAndExpirationShouldSucceed() {
        Set set = new Set();

        Assertions.assertTrue(set.call("MY_KEY", "stored_value", 10));
    }
}
