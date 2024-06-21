FROM bellsoft/liberica-openjdk-alpine:17
WORKDIR /app
COPY board.jar board.jar
EXPOSE 4000
ENTRYPOINT ["java","-jar", "-Dspring.profiles.active=prod", "board.jar"]