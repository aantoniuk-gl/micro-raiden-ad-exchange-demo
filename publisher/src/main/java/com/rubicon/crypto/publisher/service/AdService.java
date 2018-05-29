package com.rubicon.crypto.publisher.service;

import java.util.HashMap;
import java.util.Map;

import com.google.openrtb.OpenRtb.BidResponse.SeatBid.Bid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class AdService {

    private final MonitoringService monitoringService;
    private final RestTemplate restTemplate;
    private final String adServerUri;

    public AdService(
            MonitoringService monitoringService,
            RestTemplate restTemplate,
            @Value("${service.adServer.uri}") String adServerUri) {
        this.monitoringService = monitoringService;
        this.restTemplate = restTemplate;
        this.adServerUri = adServerUri;
    }

    public String getContent() {
        ResponseEntity<Bid> bidResponse = restTemplate.getForEntity(adServerUri + "/bid", Bid.class);
        Bid bid = bidResponse.getBody();

        monitoringService.log("Bid with impId=" + bid.getImpid() + " was received from Ad-Server");

        monitoringService.log("Win notification was sent to AdServer for impId=" + bid.getImpid());
        sendWinNotificationToAdServer(bid.getImpid(), bid.getNurl());

        monitoringService.log("Billing notification was sent to DSP for impId=" + bid.getImpid());
        sendBillingNotificationToDsp(bid.getBurl());

        return bid.getIurl();
    }

    private void sendWinNotificationToAdServer(String impId, String dspWinNotificationUrl) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(adServerUri + "/win")
                                                           .queryParam("impId", impId)
                                                           .queryParam("dspWinUrl", dspWinNotificationUrl);
        restTemplate.getForEntity(builder.toUriString(), String.class);
    }

    private void sendBillingNotificationToDsp(String billingUrl) {
        restTemplate.getForEntity(billingUrl, String.class);
    }
}
