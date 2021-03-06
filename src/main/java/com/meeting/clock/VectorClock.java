package com.meeting.clock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * VectorClock algorithm implementation.
 * <a href="https://en.wikipedia.org/wiki/Vector_clock">https://en.wikipedia.org/wiki/Vector_clock
 */
public class VectorClock implements Serializable {

    private static final Logger LOGGER = LogManager.getLogger(VectorClock.class);

    private final String key;
    private final Map<String, Long> clocks = new HashMap<>();

    /**
     * Construct the vectorClock initializing it.
     *
     * @param key
     */
    public VectorClock(String key) {
        this.key = key;
        initialize(key);
    }

    /**
     * Initialize this vectorClock adding a register with 0 for the specified key.
     *
     * @param key
     */
    private void initialize(String key) {
        clocks.put(key, 0L);

        LOGGER.info("VectorClock initialized: {}", this);
    }

    /**
     * Get the tick (value) from a specified key.
     *
     * @param key
     * @return
     */
    Long getTick(String key) {
        Long tick = clocks.get(key);
        if (tick == null) {
            tick = 0L;
        }
        return tick;
    }

    /**
     * Increment this vectorClock register by one for the specified key.
     *
     * @param key
     */
    public void increment(String key) {
        Long tick = getTick(key);
        clocks.put(key, tick + 1L);

        LOGGER.info("VectorClock incremented: {}", this);
    }

    /**
     * Update this vectorClock with the information from the clock parameter (other vectorClock).
     * <p>All registers in the other vectorClock are inserted in this clock if they did not exists.
     * <p>If the register exist, the biggest value between the two registers is updated in this vectorClock.
     *
     * @param vectorClock
     */
    public void update(VectorClock vectorClock) {
        increment(key);

        vectorClock.clocks.forEach((key, tack) -> {
            Long tick = getTick(key);
            clocks.put(key, (tick > tack ? tick : tack));
        });

        LOGGER.info("VectorClock updated: {}", this);
    }

    /**
     * Compares this vectorClock with the clock clock parameter (other vectorClock).
     * <p>Returns:
     * <p><b>-1</b> if this vectorClock is <b>before</b> other vectorClock
     * <p><b>0</b> if this vectorClock is <b>concurrent</b> to other vectorClock
     * <p><b>1</b> if this vectorClock is <b>after</b> other vectorClock
     *
     * @param vectorClock
     * @return
     */
    public int compare(VectorClock vectorClock) {
        boolean thisHappenedBefore = happenedBefore(this, vectorClock);
        boolean thatHappenedBefore = happenedBefore(vectorClock,this);

        if (thisHappenedBefore && !thatHappenedBefore) {
            LOGGER.info("VectorClock {} 'Happened Before' VectorClock {}.", this, vectorClock);
            return -1;
        } else if (!thisHappenedBefore && thatHappenedBefore) {
            LOGGER.info("VectorClock {} 'NOT Happened Before' VectorClock {}.", this, vectorClock);
            return 1;
        }
        LOGGER.info("VectorClock {} and VectorClock {} are 'Concurrent'.", this, vectorClock);
        return 0;
    }

    /**
     * "Happened Before" algorithm.
     *
     * @param vcX
     * @param vcY
     * @return
     */
    private static boolean happenedBefore(VectorClock vcX, VectorClock vcY) {
        boolean greater = false;
        boolean smaller = false;
        for (Map.Entry<String, Long> entry : vcY.getClocks().entrySet()) {
            Long tick = vcX.getTick(entry.getKey());
            Long tack = entry.getValue();
            if (tick > tack) { // for all z entries, VC(x)z <= VC(y)z
                greater = true;
            }
            if (tick < tack) { // at least one z entry is VC(x)z < VC(y)z
                smaller = true;
            }
        }
        return !greater && smaller;
    }

    Map<String, Long> getClocks() {
        return clocks;
    }

    @Override
    public String toString() {
        return "" + clocks;
    }
}
