Monitoring
======

Monitoring system for logging messages from several systems

# How to build 
* mvn clean install -DskipTests=true

# How to run 
* java -jar target/crypto.poc.monitoring-1.0-SNAPSHOT.jar 

By default running on port `8081`

# Endpoints:

## Logging the messages:
* Method: POST
* Endpoint: \log
* MediaType: application/json
* Body: `{"system":"test", "message":"msg"}`

Result: log the messages to `currentDir\log\log.log`