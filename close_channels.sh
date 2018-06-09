#!/usr/bin/env bash

#close all channel between Publisher and AdExchange
curl -X GET "http://localhost:8085/microraiden/channel/closeAll" &

#close all channel between AdExchange and DSP
curl -X GET "http://localhost:8084/microraiden/channel/closeAll"

echo "Channel between Ad-Exchange and Publisher has been closed"
echo "Channels between DSPs and Ad-Exchange have been closed"
