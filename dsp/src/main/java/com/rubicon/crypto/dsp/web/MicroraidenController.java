package com.rubicon.crypto.dsp.web;

import com.rubicon.crypto.dsp.service.microraiden.MicroraidenDSPSender;
import com.rubicon.crypto.monitoring.ChannelState;

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
