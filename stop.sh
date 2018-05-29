#!/usr/bin/env bash

#close all channel between Publisher and AdServer
curl -X GET "http://localhost:8085/microraiden/channel/closeAll"
#close all channel between AdServer and DSP
curl -X GET "http://localhost:8084/microraiden/channel/closeAll"

kill -9 $(ps -A | grep java | grep pts/2 | awk '{print $1}')

echo "Demo application is stopped"
