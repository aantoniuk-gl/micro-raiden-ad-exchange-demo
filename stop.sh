#!/usr/bin/env bash

./close_channels.sh

kill -9 $(ps -A | grep java | grep pts | awk '{print $1}')

echo "Demo application has been stopped"
