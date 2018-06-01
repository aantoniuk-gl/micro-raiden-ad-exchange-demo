package org.microraiden.adexchange.demo.dsp.service;

import org.microraiden.adexchange.demo.monitoring.ChannelState;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AdServerService {

    private final RestTemplate restTemplate;
    private final String adServerUri;

    public AdServerService(
            RestTemplate restTemplate,
            @Value("${service.adServer.uri}") String adServerUri) {
        this.restTemplate = restTemplate;
        this.adServerUri = adServerUri;
    }

    public void pay(ChannelState channelState) {
        HttpEntity<ChannelState> request = new HttpEntity<>(channelState);
        restTemplate.postForEntity(adServerUri + "/pay", request, String.class);
    }
}
