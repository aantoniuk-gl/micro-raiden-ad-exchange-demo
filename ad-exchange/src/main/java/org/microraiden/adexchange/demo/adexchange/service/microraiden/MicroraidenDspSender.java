package org.microraiden.adexchange.demo.adexchange.service.microraiden;

import java.util.List;
import java.util.stream.Collectors;

import org.microraiden.adexchange.demo.monitoring.ChannelState;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MicroraidenDspSender {

    private final RestTemplate restTemplate;
    private final List<String> dspUri;

    public MicroraidenDspSender(
            RestTemplate restTemplate,
            @Value("#{'${service.dsp.uri}'.split(',')}") List<String> dspUri) {
        this.restTemplate = restTemplate;
        this.dspUri = dspUri;
    }

    public List<ChannelState> createChannels(String receiverId) {
        return dspUri.stream()
                     .map(dspUri -> createChannel(dspUri, receiverId))
                     .collect(Collectors.toList());
    }

    private ChannelState createChannel(String dspUri, String receiverId) {
        ResponseEntity<ChannelState> response = restTemplate.getForEntity(
                dspUri + "/microraiden/channel/create/" + receiverId, ChannelState.class);

        return response.getBody();
    }
}
