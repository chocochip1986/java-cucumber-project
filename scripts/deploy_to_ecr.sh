#!/bin/bash


mvn clean install -DskipTests
cd deployment
docker build -t 026083545547.dkr.ecr.ap-southeast-1.amazonaws.com/test-automation:v1 .
# Login
$(aws ecr get-login --no-include-email)
docker push 026083545547.dkr.ecr.ap-southeast-1.amazonaws.com/test-automation:v1
