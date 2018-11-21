package com.meeting.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Defines the scheduler remote behaviour.
 */
public interface SchedulerRemote extends Remote {

    /**
     * Updates a schedule on the server.
     *
     * @param schedule
     * @throws RemoteException
     */
    void updateSchedule(Schedule schedule) throws RemoteException;

}
