#!/bin/bash

#This bash script is meant to run the cdit-automation test suite via command line. This means copying the test suite into a directory
#, installing all required dependencies and running via command line. Do not mistake this as running the test suite from an executable jar via
#command line.

#Required tools needed:
#-Java RunTime Environment(1.8)
#-Maven 3.6.2

function processInputArguments() {
  if [ $# -eq 0  ]
  then
    echo "No input arguments applied! Please follow required format..."
    exit_error
  fi

  for i in "${arrayOfArguments[@]}"
  do
    if [[ $i =~ ^browser=[a-zA-Z]*$ ]]
    then
      browser=$i
    fi
    if [[ $i =~ ^env=[A-Za-z]*$ ]]
    then
      env=$i
    fi
  done

  if [ -z "$browser" ]
  then
    echo "Using default browser: chrome"
    browser=chrome
  fi
  if [ -z "$env" ]
  then
    echo "Using default environment: local"
    env=local
  fi
}

function exit_error() {
  exit
}

arrayOfArguments=("$@")
processInputArguments arrayOfArguments

if [ $env = local ] || [ $env = qa ] || [ $env = dev ] || [ $env = uat ]
then
  echo "Running test suite on ${env} environment on ${browser} browser"
  mvn test -Dbrowser=$browser -Denv=$env
fi

