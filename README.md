# Message Publisher
This module reads messages from gcp cloud storage and publishes the messages to GCP Pub Sub Topic.

[![Build Status](https://travis-ci.org/rishuatgithub/MessagePublisher.svg?branch=master)](https://travis-ci.org/rishuatgithub/MessagePublisher)

#### Build

`mvn clean install`

#### Setup

`export GOOGLE_APPLICATION_CREDENTIALS = <path to your serviceaccount.json>`

#### Execution

`java -jar messagepublisher-*.jar -Dspring.profiles.active=<environment>`

- Acceptable Env: dev, uat