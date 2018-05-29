#!/bin/bash

mvn clean install -DskipTests=true

#Monitoring
java -jar monitoring/target/crypto.monitoring-1.0-SNAPSHOT.jar --server.port=8081 &
sleep 5

#DSP1
java -jar dsp/target/crypto.dsp-1.0-SNAPSHOT.jar --server.port=8082 --ethereum.account.primaryKey=1776d8d3ca4f3c6fac3a4be742a4829aa6c18b86627f5707e632458f16223565 &
#DSP2
java -jar dsp/target/crypto.dsp-1.0-SNAPSHOT.jar --server.port=8083 --ethereum.account.primaryKey=c6d0de7f58103d90dd2ada5a588a91f66c952e112b7153b5d6e7d79fbe2c1b24 &
sleep 5

#AdServer
java -jar ad-server/target/crypto.ad-server-1.0-SNAPSHOT.jar --server.port=8084 --ethereum.account.primaryKey=3121d5cbe558743e8e47a33f2f2055539ab48722c56677eb6e56a3869d277568 &
sleep 5

#Publisher
java -jar publisher/target/crypto.publisher-1.0-SNAPSHOT.jar --server.port=8085 --ethereum.account.primaryKey=26799b5d646ff1e736185507cde006e9d2b8ea64a519e97b3cca34b448c88ad9 &

sleep 100
echo "Demo application is started"