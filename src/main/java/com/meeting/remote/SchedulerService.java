package com.meeting.remote;

import com.meeting.Meeting;
import com.meeting.Scheduler;
import com.meeting.SchedulerCallback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * RMI implementation for Scheduler.
 */
public class SchedulerService implements SchedulerRemote, Scheduler {

    private static final Logger LOGGER = LogManager.getLogger(SchedulerService.class);

    private final Registry registry;

    private SchedulerCallback callback;

    public SchedulerService() throws Exception {
        this.registry = LocateRegistry.getRegistry(); // localhost
    }

    @Override
    public void register(String name, SchedulerCallback callback) throws Exception {
        this.callback = callback;

        // Export the remote object
        SchedulerRemote stub = (SchedulerRemote) UnicastRemoteObject.exportObject(this, 0);

        // Bind the remote object's stub in the registry
        registry.rebind(name, stub);

        LOGGER.debug("{} is in!", name);
    }

    @Override
    public void unregister(String name) throws Exception {
        this.callback = null;

        registry.unbind(name);

        LOGGER.debug("{} is out!", name);
    }

    @Override
    public String[] getParticipants() throws Exception {
        return registry.list();
    }

    @Override
    public void updateMeeting(Meeting meeting, String... names) throws Exception {
        for (String to : names) {
            // register the server
            SchedulerRemote stub = (SchedulerRemote) registry.lookup(to);

            Schedule schedule = new Schedule(to, meeting);
            LOGGER.debug("Sending schedule: {}!", schedule);

            // execute the remote method
            stub.updateSchedule(schedule);
        }
    }

    @Override
    public void updateSchedule(Schedule schedule) throws RemoteException {
        if (callback == null) {
            throw new RemoteException("Service not registered.");
        }
        LOGGER.debug("Schedule received: {}!", schedule);

        callback.onMeetingUpdated(schedule.getMeeting());
    }
}
