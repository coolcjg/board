FROM openjdk:17-alpine AS builder
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 4000
ENTRYPOINT ["java","-jar","/app.jar"]