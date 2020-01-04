# Chess
## Simulates and validates Chess game-play according to provided moves

### Project Uses:
* Java 1.8 / Java Swing
* Maven


### Dependencies: 
* userinput.jar

### Adding userinput.jar to local maven
    mvn install:install-file -Dfile=libs/userinput.jar -DgroupId=com.whg.userinput  -DartifactId=userinput-engine -Dversion=1.0 -Dpackaging=jar


### Library directory:
*   /libs

### Game records directory:
 *   /data
 

# Overview
This application provides a solution for simulating two players chess game. It validates and controls the process.
At the beginning, it asks to select and open the file which contains a game record.
After that game/simulation will be started and whenever an invalid move occurs or player gets/gives check game will be stopped.
