FROM openjdk:17-alpine AS builder
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app1.jar
ENTRYPOINT ["java","-jar","/app.jar"]