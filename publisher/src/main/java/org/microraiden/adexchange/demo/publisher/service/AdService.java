package org.microraiden.adexchange.demo.publisher.service;

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
    private final String adExchangeUri;

    public AdService(
            MonitoringService monitoringService,
            RestTemplate restTemplate,
            @Value("${service.adExchange.uri}") String adExchangeUri) {
        this.monitoringService = monitoringService;
        this.restTemplate = restTemplate;
        this.adExchangeUri = adExchangeUri;
    }

    public String getContent() {
        ResponseEntity<Bid> bidResponse = restTemplate.getForEntity(adExchangeUri + "/bid", Bid.class);
        Bid bid = bidResponse.getBody();

        monitoringService.log("Bid with impId=" + bid.getImpid() + " was received from Ad-Exchange");

        monitoringService.log("Win notification was sent to AdExchange for impId=" + bid.getImpid());
        sendWinNotificationToAdExchange(bid.getImpid(), bid.getNurl());

        monitoringService.log("Billing notification was sent to DSP for impId=" + bid.getImpid());
        sendBillingNotificationToDsp(bid.getBurl());

        return bid.getIurl();
    }

    private void sendWinNotificationToAdExchange(String impId, String dspWinNotificationUrl) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(adExchangeUri + "/win")
                                                           .queryParam("impId", impId)
                                                           .queryParam("dspWinUrl", dspWinNotificationUrl);
        restTemplate.getForEntity(builder.toUriString(), String.class);
    }

    private void sendBillingNotificationToDsp(String billingUrl) {
        restTemplate.getForEntity(billingUrl, String.class);
    }
}
