FROM openjdk:8
#ENV = {docker_local, qa, uat, dev}
ARG env=docker
ENV env=${env}
COPY lib /lib/
COPY uber-cdit-automation.jar /uber-cdit-automation.jar
WORKDIR .
RUN echo "Creating docker image for test environment: ${env}"
ENTRYPOINT java -Dspring.profiles.active=${env} -cp "/uber-cdit-automation.jar:/lib/*" "cdit_automation.RunCucumberTest"