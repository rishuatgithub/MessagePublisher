FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/messagepublisher-*.jar
WORKDIR /app
COPY ${JAR_FILE} messagepublisher.jar
ENTRYPOINT ["java","-jar","/app/messagepublisher.jar"]