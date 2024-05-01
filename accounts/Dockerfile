#start with a base image containing Java runtime
FROM openjdk:17-jdk-slim

#information around who maintains the image
MAINTAINER kollo.zsolt@codespring.ro

#add the application's jar to the image
COPY target/accounts-0.0.1-SNAPSHOT.jar accounts-0.0.1-SNAPSHOT.jar

#execute the application
ENTRYPOINT ["java", "-jar", "accounts-0.0.1-SNAPSHOT.jar"]