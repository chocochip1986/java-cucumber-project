FROM openjdk:8
WORKDIR /app
COPY lib /app/lib/
COPY artifacts /app/src/main/resources/artifacts/
COPY uber-cdit-automation.jar /app/uber-cdit-automation.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=automation", "-cp", "/app/uber-cdit-automation.jar:/app/lib/*", "automation.RunCucumberTest" ]
