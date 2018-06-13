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

#AdExchange
java -jar ad-exchange/target/ad-exchange.demo.ad-exchange-1.0-SNAPSHOT.jar --server.port=8084 --ethereum.account.primaryKey=1d2c6757f774ff79c9460602e3fd5773172ec20d328534970a305fc9aa353744 &
sleep 10

#Publisher
java -jar publisher/target/ad-exchange.demo.publisher-1.0-SNAPSHOT.jar --server.port=8085 --ethereum.account.primaryKey=30f22909521a89eeb027dd948f85f5e8e642d36e13f00b7ae93e72f849b1a78c &
sleep 10

./status.sh