#!/usr/bin/env bash

#create channel between AdExchange and Publisher
curl -X GET "http://localhost:8085/microraiden/channel/create"

echo "Channel between Ad-Exchange and Publisher has been created"

#create channels between DSP and AdExchange
curl -X GET "http://localhost:8084/microraiden/channel/create"

echo "Channels between DSPs and Ad-Exchange have been created"
