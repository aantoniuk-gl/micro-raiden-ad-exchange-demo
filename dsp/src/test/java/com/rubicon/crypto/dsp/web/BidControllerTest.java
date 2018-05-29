package com.rubicon.crypto.dsp.web;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;

import com.google.openrtb.OpenRtb.BidResponse.SeatBid.Bid;
import com.rubicon.crypto.dsp.Application;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BidControllerTest {

    private final String hostAddress;

    @LocalServerPort
    private int port;

    private RestTemplate restTemplate = new RestTemplate(Collections.singletonList(new ProtobufHttpMessageConverter()));

    public BidControllerTest() throws UnknownHostException {
        hostAddress = InetAddress.getLocalHost().getHostAddress();
    }

    @Test
    public void testValidBid() {

        String impId = "1";

        ResponseEntity<Bid> bidResponse = restTemplate.getForEntity("http://localhost:" + port + "/bid/" + impId, Bid.class);
        assertNotNull(bidResponse);

        Bid bid = bidResponse.getBody();
        assertNotNull(bid);

        assertEquals(impId, bid.getImpid());
        assertTrue(bid.getPrice() >= 1 && bid.getPrice() <= 100);
        assertEquals("http://" + hostAddress + ":" + port + "/win/" + impId, bid.getNurl());
        assertEquals("http://" + hostAddress + ":" + port + "/billing/" + impId, bid.getBurl());
        assertNotNull(bid.getIurl());
    }
}