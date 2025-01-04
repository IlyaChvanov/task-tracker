FROM openjdk:17

WORKDIR /app

COPY build/libs/task-tracker-api-1.0.0.jar task-tracker-api.jar
COPY src/main/resources resources

CMD ["java", "-jar", "task-tracker-api.jar"]