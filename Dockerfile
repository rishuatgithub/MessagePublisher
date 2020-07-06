### Docker file for build and package - Java / Maven
### Build
FROM maven:3.6.1-jdk-8-alpine AS build
ARG BUILD_DIR=/app/build
COPY src ${BUILD_DIR}/src
COPY pom.xml ${BUILD_DIR}
WORKDIR /app
RUN mvn -f ${BUILD_DIR}/pom.xml clean install

### package
FROM openjdk:8-jdk-alpine AS package
ARG JAR_FILE=/app/build/target/messagepublisher-1.0-SNAPSHOT.jar
ARG PUBLISH_DIR=/app/dock
WORKDIR /app
COPY --from=build ${JAR_FILE} ${PUBLISH_DIR}/messagepublisher.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/dock/messagepublisher.jar"]