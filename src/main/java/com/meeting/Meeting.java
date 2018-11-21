package com.meeting;

import com.meeting.clock.VectorClock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.time.DayOfWeek;

/**
 * A Meeting is a local entity which have a user and date (DayOfWeek).
 * It can be updated setting the date or from other meeting object.
 *
 * <p>When it is updated by date, it increments the VectorClock's register related to the current user.
 * <p>When it is updated by other meeting, it update all VectorClock's registers.
 */
public class Meeting implements Serializable {

    private static final Logger LOGGER = LogManager.getLogger(Meeting.class);

    private DayOfWeek dayOfWeek;

    private final String user;

    private final VectorClock vectorClock;

    /**
     * Constructs a Meeting, initializing the VectorClock.
     *
     * @param user
     */
    public Meeting(String user) {
        this.user = user;
        this.vectorClock = new VectorClock(user);
    }

    /**
     * Updates this meeting and increments the vector clock for the current user.
     *
     * @param dayOfWeek
     */
    public void update(int dayOfWeek) {
        update(DayOfWeek.of(dayOfWeek));
    }

    /**
     * Updates this meeting and increments the vector clock for the current user.
     *
     * @param dayOfWeek
     */
    public void update(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
        this.vectorClock.increment(user);

        LOGGER.info("Meeting updated: {}", this);
    }

    /**
     * Update this meeting and vectorClock from the received meeting only if
     * this meeting happened before the other meeting.
     *
     * @param meeting
     */
    public void update(Meeting meeting) {
        update(meeting, false);
    }


    /**
     * (Always) Update this meeting and vectorClock from the received meeting.
     *
     * @param meeting
     */
    public void updateConcur(Meeting meeting) {
        update(meeting, true);
    }

    /**
     * Updates this meeting and vectorClock from the received meeting considering the concurrency.
     * <p> If both meetings are concurrent, only update when 'concurrent' flag is true.
     *
     * @param meeting
     * @param concurrent
     */
    private void update(Meeting meeting, boolean concurrent) {
        int concurrency = this.vectorClock.compare(meeting.vectorClock);
        if (concurrency < 0 || concurrent) {
            this.dayOfWeek = meeting.dayOfWeek;
            this.vectorClock.update(meeting.vectorClock);

            LOGGER.info("Meeting updated: {}", this);
        } else {
            LOGGER.warn("Meeting not updated due to concurrency!");
        }
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public String getUser() {
        return user;
    }

    public VectorClock getVectorClock() {
        return vectorClock;
    }

    @Override
    public String toString() {
        return dayOfWeek != null ? "DayOfWeek=" + dayOfWeek : "NONE";
    }
}
