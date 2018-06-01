package org.microraiden.adexchange.demo.dsp.web;

import org.microraiden.adexchange.demo.dsp.service.microraiden.MicroraidenDSPSender;
import org.microraiden.adexchange.demo.monitoring.ChannelState;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MicroraidenController {

    private final MicroraidenDSPSender microraidenDSPSender;

    public MicroraidenController(MicroraidenDSPSender microraidenDSPSender) {
        this.microraidenDSPSender = microraidenDSPSender;
    }

    @GetMapping("/microraiden/channel/create/{receiverAccountId}")
    public ChannelState createChannel(@PathVariable String receiverAccountId) {
        return microraidenDSPSender.createChannel(receiverAccountId);
    }
}
