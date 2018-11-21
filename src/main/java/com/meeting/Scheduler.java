package com.meeting;

/**
 * Defines the scheduler behaviour
 */
public interface Scheduler {

    /**
     * Register a user using its name and defining the callback object to receive the schedules.
     *
     * @param name
     * @param callback
     * @throws Exception
     */
    void register(String name, SchedulerCallback callback) throws Exception;

    /**
     * Unregister a user using its name.
     *
     * @param name
     * @throws Exception
     */
    void unregister(String name) throws Exception;

    /**
     * Gets the list of users registered in this Scheduler.
     *
     * @return
     * @throws Exception
     */
    String[] getParticipants() throws Exception;

    /**
     * Updates the meeting information, sending the new value to a specific list of users.
     *
     * @param meeting
     * @param users
     * @throws Exception
     */
    void updateMeeting(Meeting meeting, String... users) throws Exception;
}
