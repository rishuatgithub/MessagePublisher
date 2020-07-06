# Message Publisher
This module reads messages from gcp cloud storage and publishes the messages to GCP Pub Sub Topic.

#### Build

`mvn clean install`

#### Execution

`java -jar messagepublisher-*.jar -Dspring.profiles.active=<environment>`

- Acceptable Env: dev, uat