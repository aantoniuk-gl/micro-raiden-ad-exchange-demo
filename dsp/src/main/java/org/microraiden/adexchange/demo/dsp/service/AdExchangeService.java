package org.microraiden.adexchange.demo.dsp.service;

import org.microraiden.adexchange.demo.monitoring.ChannelState;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AdExchangeService {

    private final RestTemplate restTemplate;
    private final String adExchangeUri;

    public AdExchangeService(
            RestTemplate restTemplate,
            @Value("${service.adExchange.uri}") String adExchangeUri) {
        this.restTemplate = restTemplate;
        this.adExchangeUri = adExchangeUri;
    }

    public void pay(ChannelState channelState) {
        HttpEntity<ChannelState> request = new HttpEntity<>(channelState);
        restTemplate.postForEntity(adExchangeUri + "/pay", request, String.class);
    }
}
