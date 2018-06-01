#!/usr/bin/env bash

#close all channel between Publisher and AdServer
curl -X GET "http://localhost:8085/microraiden/channel/closeAll"

echo "Channel between Ad-Server and Publisher has been closed"

#close all channel between AdServer and DSP
curl -X GET "http://localhost:8084/microraiden/channel/closeAll"

echo "Channels between DSPs and Ad-Server have been closed"
