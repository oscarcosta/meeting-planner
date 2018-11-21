package com.meeting;

/**
 * Defines a callback method for a scheduler
 */
public interface SchedulerCallback {

    /**
     * Callback function to update the meeting status for the scheduler clients.
     *
     * @param meeting
     */
    void onMeetingUpdated(Meeting meeting);
}
