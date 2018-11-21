package com.meeting;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;

public class MeetingTest {

    @Test
    public void shouldCreateMeeting() {
        Meeting meeting = new Meeting("test");

        Assertions.assertNotNull(meeting.getUser());
        Assertions.assertNotNull(meeting.getVectorClock());
        Assertions.assertNull(meeting.getDayOfWeek());
    }

    @Test
    public void shouldUpdateMeetingWithInteger() {
        Meeting meeting = new Meeting("test");

        meeting.update(1);

        Assertions.assertEquals(DayOfWeek.MONDAY, meeting.getDayOfWeek());
    }

    @Test
    public void shouldUpdateMeetingWithDayOfWeek() {
        Meeting meeting = new Meeting("test");

        meeting.update(DayOfWeek.MONDAY);

        Assertions.assertEquals(DayOfWeek.MONDAY, meeting.getDayOfWeek());
    }

    @Test
    public void shouldUpdateMeetingWithOtherMeeting() {
        Meeting meetingA = new Meeting("A");
        Meeting meetingB = new Meeting("B");

        meetingA.update(DayOfWeek.MONDAY);  // A selected MONDAY
        meetingB.update(meetingA);          // B received MONDAY from A

        Assertions.assertEquals(DayOfWeek.MONDAY, meetingB.getDayOfWeek());
    }

    @Test
    public void shouldUpdateConcurrentlyMeeting() {
        Meeting meetingA = new Meeting("A");
        Meeting meetingB = new Meeting("B");

        meetingA.update(DayOfWeek.MONDAY);  // A selected MONDAY
        meetingB.update(DayOfWeek.SUNDAY);  // B selected SUNDAY
        meetingB.updateConcur(meetingA);    // B received MONDAY from A

        Assertions.assertEquals(DayOfWeek.MONDAY, meetingB.getDayOfWeek()); // it was updated
    }

    @Test
    public void shouldNotUpdateConcurrentlyMeeting() {
        Meeting meetingA = new Meeting("A");
        Meeting meetingB = new Meeting("B");

        meetingA.update(DayOfWeek.MONDAY);  // A selected MONDAY
        meetingB.update(DayOfWeek.SUNDAY);  // B selected SUNDAY
        meetingB.update(meetingA);          // B received MONDAY from A

        Assertions.assertNotEquals(DayOfWeek.MONDAY, meetingB.getDayOfWeek()); // it was NOT updated
    }

    @Test
    public void shouldObeyEventsOrder() {
        Meeting meetingA = new Meeting("A");
        Meeting meetingB = new Meeting("B");
        Meeting meetingC = new Meeting("C");

        meetingA.update(DayOfWeek.MONDAY);  // A selected MONDAY
        meetingB.update(meetingA);          // B received MONDAY from A
        meetingC.update(meetingA);          // C received MONDAY from A

        meetingB.update(DayOfWeek.WEDNESDAY);   // B select WEDNESDAY
        meetingC.update(meetingB);              // C received WEDNESDAY from B

        meetingC.update(DayOfWeek.FRIDAY);  // C select FRIDAY
        meetingA.update(meetingC);          // A received FRIDAY from C
        meetingA.update(meetingB);          // A received WEDNESDAY from B
        meetingB.update(meetingC);          // B received FRIDAY from C

        Assertions.assertEquals(DayOfWeek.FRIDAY, meetingA.getDayOfWeek()); // it has the last event
        Assertions.assertEquals(DayOfWeek.FRIDAY, meetingB.getDayOfWeek());
        Assertions.assertEquals(DayOfWeek.FRIDAY, meetingC.getDayOfWeek());
    }

}
