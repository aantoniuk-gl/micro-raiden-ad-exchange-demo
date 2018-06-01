#!/usr/bin/env bash

#create channel between AdServer and Publisher
curl -X GET "http://localhost:8085/microraiden/channel/create"

echo "Channel between Ad-Server and Publisher has been created"

#create channels between DSP and AdServer
curl -X GET "http://localhost:8084/microraiden/channel/create"

echo "Channels between DSPs and Ad-Server have been created"
