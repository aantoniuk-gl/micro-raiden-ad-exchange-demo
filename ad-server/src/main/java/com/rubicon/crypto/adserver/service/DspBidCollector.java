package com.rubicon.crypto.adserver.service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

import com.google.openrtb.OpenRtb.BidResponse.SeatBid.Bid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DspBidCollector {

    private final MonitoringService monitoringService;
    private final RestTemplate restTemplate;
    private final List<String> dspUri;

    public DspBidCollector(
            MonitoringService monitoringService,
            RestTemplate restTemplate,
            @Value("#{'${service.dsp.uri}'.split(',')}") List<String> dspUri) {
        this.monitoringService = monitoringService;
        this.restTemplate = restTemplate;
        this.dspUri = dspUri;
    }

    public Bid collect() {
        String impId = UUID.randomUUID().toString();

        Stream<Bid> bidStream = getBidsFromDsps(impId);

        Bid bid = bidStream.max(Comparator.comparing(Bid::getPrice)).orElseGet(() -> {
            monitoringService.log("No bids");

            return null;
        });

        monitoringService.log("Bid won with id=" + bid.getId() + " for impId=" + bid.getImpid());

        return bid;
    }

    public void sendWinNotification(String impId, String dspWinNotificationUrl) {
        monitoringService.log("Win notification was sent From AdServer to DSP for impId=" + impId);

        restTemplate.getForEntity(dspWinNotificationUrl, String.class);
    }

    private Stream<Bid> getBidsFromDsps(String impId) {
        return dspUri.stream()
                     .map(dspUri -> getBidFromDsp(impId, dspUri))
                     .filter(Objects::nonNull);
    }

    private Bid getBidFromDsp(String impId, String dspUrl) {
        ResponseEntity<Bid> bidResponse;
        try {
            bidResponse = restTemplate.getForEntity(dspUrl + "/bid/" + impId, Bid.class);
        } catch (Exception e) {
            monitoringService.log("Couldn't connect to DSP:" + dspUrl);
            return null;
        }
        Bid bid = bidResponse.getBody();

        if (bid != null) {
            monitoringService.log("Bid with impId=" + bid.getImpid() + " was received from DSP");
        }
        return bid;
    }
}
