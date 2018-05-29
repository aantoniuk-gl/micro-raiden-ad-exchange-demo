package com.rubicon.crypto.dsp.service;

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
    private static final String[] CONTENT = { "https://www.google.com.ua/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png"
            , "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR8KwBSYHpAkJ8DhOuPIrlAqf6NbuR9bsUoWtXy4nHulzzKbM8EUA"
            , "https://www.facebook.com/images/fb_icon_325x325.png" };

    private final int port;

    public ResponseBuilder(@Value("${server.port}") int port) {
        this.port = port;
    }

    public Bid build(String impId) {
        Bid bid = Bid.newBuilder()
                     .setId(UUID.randomUUID().toString())
                     .setImpid(impId)
                     .setPrice(ThreadLocalRandom.current().nextInt(2, 5))
                     .setIurl(CONTENT[ThreadLocalRandom.current().nextInt(0, 3)])
                     .setNurl("http://localhost:" + port + "/win/" + impId)
                     .setBurl("http://localhost:" + port + "/billing/" + impId)
                     .build();
        LOGGER.debug("Bid was created:\n{}", bid);

        return bid;
    }
}
