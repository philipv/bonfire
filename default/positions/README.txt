Project Description:
This is a command line based system which takes input from command line and file(optionally) and aggregate the results and show the final positions for individual currency 
codes. Sample inputs:
usd 321.4
HKD 321.4

Criteria for the input:
 - Should be two strings separated by a space.
 - First string represent the currency code and should be 3 character long. This is case insensitive. There is no limitation on what these 3 character currency code could 
 be. But could easily maintain an acceptable set of currency codes, if required.
 - Second string is the double value representing the position update. This field is aggregated based on per currency code in the final position report.
 - If there is anything wrong in the formatting of the input or non-number values then it prints a message showing the problem and then continues to wait for a new input.
 - If "quit" is entered then the program exits.
 - Every 60 seconds we print the positions per currency on the console and if its zero then the position for that currency is not printed.
 
 Software Requirements for building and running the project:
 - JDK 6 or higher
 - Maven 3.0.4 or higher
 - Functional local maven repository which could sync up with online central maven repository
 
 Steps to build and run this application:
 - Unzip the file into a folder.
 - This is an eclipse project so you can import extracted folder as an Eclipse Maven project (will also need m2e plugin in Eclipse). Since this project was created using 
 Eclipse Juno, therefore can have some issues with older versions of Eclipse.
 - Open command line (based on the environment) and go to the folder containing pom.xml
 - Run this command to build and execute the test cases:
 	mvn clean compile install
 - To run the command line system using maven, without using any file input, from the folder containing pom.xml
 	mvn exec:java -Dexec.mainClass="com.bonfire.source.PositionReceiver"
 - To run the command line system using maven, using a file input too, from the folder containing pom.xml (also note that sample.txt is available in the same directory as 
 pom.xml)
 	mvn exec:java -Dexec.mainClass="com.bonfire.source.PositionReceiver" -Dexec.args="sample.txt"
 	
External Libraries used (which will be automatically resolved from mvn central repository):
 - Apache Commons Lang 3.1
 - JUnit 4.11
 - Mockito 1.9.5
 
Code Coverage for the project: 93.4%
