FROM openjdk:8
COPY lib /lib/
COPY uber-cdit-automation.jar /uber-cdit-automation.jar
WORKDIR .
ENTRYPOINT ["java", "-cp", "/uber-cdit-automation.jar:/lib/*", "cdit_automation.RunCucumberTest"]