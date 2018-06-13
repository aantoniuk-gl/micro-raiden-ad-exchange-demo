package org.microraiden.adexchange.demo.dsp.service;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import com.google.openrtb.OpenRtb.BidResponse.SeatBid.Bid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ResponseBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseBuilder.class);

    private final int port;
    private final String appName;

    public ResponseBuilder(
            @Value("${server.port}") int port,
            @Value("${spring.application.name}") String appName) {
        this.port = port;
        this.appName = appName;
    }

    public Bid build(String impId) {
        Bid bid = Bid.newBuilder()
                     .setId(UUID.randomUUID().toString())
                     .setImpid(impId)
                     .setPrice(ThreadLocalRandom.current().nextInt(2, 6))
                     .setIurl("http://localhost:" + port + "/" + appName + ".png")
                     .setNurl("http://localhost:" + port + "/win/" + impId)
                     .setBurl("http://localhost:" + port + "/billing/" + impId)
                     .setAdid(appName)
                     .build();
        LOGGER.debug("Bid was created:\n{}", bid);

        return bid;
    }
}
