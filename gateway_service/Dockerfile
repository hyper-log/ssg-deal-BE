FROM --platform=linux/amd64 openjdk:17-jdk-alpine

WORKDIR /app

ARG JAR_FILE=build/libs/gateway_service-1.0-SNAPSHOT.jar
ARG APP_YML=src/main/resources/application.yml

COPY ${JAR_FILE} app.jar
COPY ${APP_YML} application.yml

ENTRYPOINT ["java", "-jar", "app.jar"]