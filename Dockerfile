FROM openjdk:8-jdk-alphine
ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} messagepublisher.jar

ENTRYPOINT ["java","-jar","/messagepublisher.jar"]