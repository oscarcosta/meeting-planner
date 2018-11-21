# Meeting Planner with Vector Clocks

### Build 
The system is build using *Gradle version 4.x* using the command:

`$ ./gradlew build`                                                          

The jar file will be generated inside the folder **build/lib/**.

### Run
To execute the system, firstly is necessary run the *rmiregistry* with the jar file in the *CLASSPATH*:

`$ CLASSPATH=./build/libs/MeetingPlanner-1.0-SNAPSHOT.jar rmiregistry`       

Then, each participant has to be run in a different terminal, passing its name as parameter. For example:

`$ java -jar build/libs/MeetingPlanner-1.0-SNAPSHOT.jar Alice`               

The system options will be showed at  the console.

*Disclaimer: To keep it simple, the system runs only on localhost.*