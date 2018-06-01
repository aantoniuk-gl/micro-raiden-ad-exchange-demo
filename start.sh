#!/bin/bash

mvn clean install -DskipTests=true

#Monitoring
java -jar monitoring/target/ad-exchange.demo.monitoring-1.0-SNAPSHOT.jar --server.port=8081 &
sleep 10

#DSP1
java -jar dsp/target/ad-exchange.demo.dsp-1.0-SNAPSHOT.jar --server.port=8082 --spring.application.name=dsp1 --ethereum.account.primaryKey=5ddb33cc59b62dd024be2108ee28bb5e3127321ba8c368f31f5c283fca11841b &
sleep 10

#DSP2
java -jar dsp/target/ad-exchange.demo.dsp-1.0-SNAPSHOT.jar --server.port=8083 --spring.application.name=dsp2 --ethereum.account.primaryKey=c6d0de7f58103d90dd2ada5a588a91f66c952e112b7153b5d6e7d79fbe2c1b24 &
sleep 10

#AdServer
java -jar ad-server/target/ad-exchange.demo.ad-server-1.0-SNAPSHOT.jar --server.port=8084 --ethereum.account.primaryKey=1d2c6757f774ff79c9460602e3fd5773172ec20d328534970a305fc9aa353744 &
sleep 10

#Publisher
java -jar publisher/target/ad-exchange.demo.publisher-1.0-SNAPSHOT.jar --server.port=8085 --ethereum.account.primaryKey=26799b5d646ff1e736185507cde006e9d2b8ea64a519e97b3cca34b448c88ad9 &
sleep 10


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
    echo "Ad-Server Service has been started"
else
    echo "Ad-Server Service hasn't been started"
fi
STATUS=$(curl -s -o /dev/null -w '%{http_code}' http://localhost:8085/actuator/health)
if [ $STATUS -eq 200 ]; then
    echo "Publisher Service has been started"
else
    echo "Publisher Service hasn't been started"
fi