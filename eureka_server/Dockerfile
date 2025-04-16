FROM --platform=linux/amd64 openjdk:17-jdk-alpine

WORKDIR /app

ARG JAR_FILE=build/libs/eureka_server-1.0-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]