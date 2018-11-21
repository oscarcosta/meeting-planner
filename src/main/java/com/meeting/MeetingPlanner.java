package com.meeting;

import com.meeting.remote.SchedulerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import java.io.Console;
import java.util.Arrays;


/**
 * MeetingPlanner: main class
 */
public class MeetingPlanner implements SchedulerCallback {

    private static final Logger LOGGER = LogManager.getLogger(MeetingPlanner.class);

    private final Scheduler scheduler;

    private final Meeting meeting;

    private MeetingPlanner(String user) throws Exception {
        this.scheduler = new SchedulerService();
        this.meeting = new Meeting(user);
    }

    /**
     * Main method (Smoke test):
     *
     * <ol>
     * <li>run rmiregistry with jar in CLASSPATH
     * CLASSPATH=./build/libs/MeetingPlanner-1.0-SNAPSHOT.jar rmiregistry
     * <li>run agents (server/client)
     * java -jar build/libs/MeetingPlanner-1.0-SNAPSHOT.jar Alice
     * <li>follow console instructions
     *
     * @param args
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("USAGE:\n java -jar MeetingPlanner-1.0-SNAPSHOT.jar <user_name>");
            System.exit(-1);
        }

        System.setProperty("logFilename", args[0]); // separate the logs file per execution
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        ctx.reconfigure();

        try {
            MeetingPlanner executor = new MeetingPlanner(args[0]);
            executor.runServer();
            executor.runClient();
        } catch (Exception e) {
            LOGGER.error("Error running MeetingPlanner:", e);
            System.exit(-1);
        }
    }

    private void runServer() throws Exception {
        scheduler.register(meeting.getUser(), this);
    }

    private void runClient() {
        while (true) {
            Console console = System.console();

            String option = console.readLine("\nSelect an option:\n U = update meeting, Q = quit program\n");
            switch (option) {
                case "u":
                case "U":
                    updateMeeting(console);
                    break;
                case "q":
                case "Q":
                    quitProgram();
                    break;
                default:
                    System.err.print("Wrong option! Try again...\n");
            }
        }
    }

    private void updateMeeting(Console console) {
        try {
            System.out.printf("Available participants: %s\n", Arrays.toString(scheduler.getParticipants()));
            String participants = console.readLine("Select one or more participants (separated by space): ");

            System.out.printf("Current meeting day: %s\n", meeting);
            System.out.print("Available days: [1 = MONDAY, 2 = TUESDAY, 3 = WEDNESDAY, 4 = THURSDAY, 5 = FRIDAY, 6 = SATURDAY, 7 = SUNDAY]\n");
            int day = Integer.parseInt(console.readLine("Select a day: " ));

            meeting.update(day);

            scheduler.updateMeeting(meeting, participants.split("\\s+"));
            System.out.println("Update sent!");
        } catch (Exception e) {
            System.out.printf("Error updating meeting: '%s'.\n", e.getMessage());
            LOGGER.error("Error updating meeting:", e);
        }
    }

    @Override
    public void onMeetingUpdated(Meeting meeting) {
        this.meeting.update(meeting);
    }

    private void quitProgram() {
        try {
            scheduler.unregister(meeting.getUser());

            System.out.printf("Bye %s!\n\n", meeting.getUser());
        } catch (Exception e) {
            LOGGER.error("Error quiting program:", e);
        }
        System.exit(0);
    }
}
