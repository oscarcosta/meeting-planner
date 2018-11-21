package com.meeting.clock;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class VectorClockTest {

    @Test
    void shouldContainKeyOnInitializeVectorClock() {
        VectorClock clock = new VectorClock("key");

        Assertions.assertTrue(clock.getClocks().containsKey("key"));
    }

    @Test
    void shouldHaveOneOnIncrementVectorClock() {
        VectorClock clock = new VectorClock("key");

        clock.increment("key");

        Assertions.assertEquals(new Long("1"), clock.getTick("key"));
    }

    @Test
    void shouldHaveValueOnUpdateVectorClock() {
        VectorClock clockA = new VectorClock("A");
        VectorClock clockB = new VectorClock("B");

        clockA.increment("A");
        clockB.increment("B");

        Assertions.assertFalse(clockA.getClocks().containsKey("B")); // A does not contains B

        clockA.update(clockB); // A is updated by B

        Assertions.assertTrue(clockA.getClocks().containsKey("B")); // A contains B
    }

    @Test
    void shouldIdentifyHappenedBefore() {
        VectorClock clockA = new VectorClock("A"); // A:0
        VectorClock clockB = new VectorClock("B"); // B:0

        clockA.increment("A");  // A:1
        clockB.update(clockA);  // A:1, B:0
        clockB.increment("B");  // A:1, B:1

        // clockA=[A:1,'B:0'] < clockB=[A:1,B:1]
        Assertions.assertEquals(-1, clockA.compare(clockB));
    }

    @Test
    void shouldIdentifyHappenedAfter() {
        VectorClock clockA = new VectorClock("A"); // A:0
        VectorClock clockB = new VectorClock("B"); // B:0

        clockA.increment("A");  // A:1
        clockB.increment("B");  // B:1
        clockA.update(clockB);  // A:1, B:1

        // clockA=[A:1,B:1] < clockB=['A:0',B:1]
        Assertions.assertEquals(1, clockA.compare(clockB));
    }

    @Test
    void shouldIdentifyConcurrency() {
        VectorClock clockA = new VectorClock("A"); // A:0
        VectorClock clockB = new VectorClock("B"); // B:0

        clockA.increment("A");  // A:1
        clockB.increment("B");  // B:1

        // clockA=[A:1,'B:0'] != clockB=['A:0',B:1]
        Assertions.assertEquals(0, clockA.compare(clockB));
        Assertions.assertEquals(0, clockB.compare(clockA));
    }

}
