# Chess
## Simulates and validates Chess game-play according to provided moves

### Project Uses:
* Java 1.8
* Java Swing
* Maven



### Dependencies: 
* userinput.jar

### adding userinput.jar to local maven
    mvn install:install-file -Dfile=libs/userinput.jar -DgroupId=com.whg.userinput  -DartifactId=userinput-engine -Dversion=1.0 -Dpackaging=jar

# Overview
After that game/simulation will be started and whenever an invalid move occurs or player gets / gives check game will stop.