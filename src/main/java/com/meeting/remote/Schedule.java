package com.meeting.remote;

import com.meeting.Meeting;

import java.io.Serializable;

/**
 * Defines the message to be send among clients and servers.
 */
public class Schedule implements Serializable {

    private final String to;
    private final Meeting meeting;

    public Schedule(String to, Meeting meeting) {
        this.to = to;
        this.meeting = meeting;
    }

    public String getTo() {
        return to;
    }

    public Meeting getMeeting() {
        return meeting;
    }

    @Override
    public String toString() {
        return "Schedule={" +
                ", to='" + to + '\'' +
                ", meeting=" + meeting +
                '}';
    }
}
