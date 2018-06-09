package org.microraiden.adexchange.demo.adexchange.service;

import org.microraiden.adexchange.demo.monitoring.ChannelState;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PublisherService {

    private final RestTemplate restTemplate;
    private final String publisherUri;

    public PublisherService(
            RestTemplate restTemplate,
            @Value("${service.publisher.uri}") String publisherUri) {
        this.restTemplate = restTemplate;
        this.publisherUri = publisherUri;
    }

    public void pay(ChannelState channelState) {
        HttpEntity<ChannelState> request = new HttpEntity<>(channelState);
        restTemplate.postForEntity(publisherUri + "/pay", request, String.class);
    }
}
