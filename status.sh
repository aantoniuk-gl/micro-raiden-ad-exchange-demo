#!/bin/bash

STATUS=$(curl -s -o /dev/null -w '%{http_code}' http://localhost:8081/actuator/health)
if [ $STATUS -eq 200 ]; then
    echo "Monitoring Service has been started"
else
    echo "Monitoring Service hasn't been started"
fi
STATUS=$(curl -s -o /dev/null -w '%{http_code}' http://localhost:8082/actuator/health)
if [ $STATUS -eq 200 ]; then
    echo "DSP1 Service has been started"
else
    echo "DSP1 Service hasn't been started"
fi
STATUS=$(curl -s -o /dev/null -w '%{http_code}' http://localhost:8083/actuator/health)
if [ $STATUS -eq 200 ]; then
    echo "DSP2 Service has been started"
else
    echo "DSP2 Service hasn't been started"
fi
STATUS=$(curl -s -o /dev/null -w '%{http_code}' http://localhost:8084/actuator/health)
if [ $STATUS -eq 200 ]; then
    echo "Ad-Exchange Service has been started"
else
    echo "Ad-Exchange Service hasn't been started"
fi
STATUS=$(curl -s -o /dev/null -w '%{http_code}' http://localhost:8085/actuator/health)
if [ $STATUS -eq 200 ]; then
    echo "Publisher Service has been started"
else
    echo "Publisher Service hasn't been started"
fi