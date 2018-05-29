package com.rubicon.crypto.adserver.web;

import com.rubicon.crypto.adserver.service.microraiden.MicroraidenAdServerReceiver;
import com.rubicon.crypto.adserver.service.microraiden.MicroraidenAdServerSender;
import com.rubicon.crypto.monitoring.ChannelState;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MicroraidenController {

    private final MicroraidenAdServerSender microraidenAdServerSender;
    private final MicroraidenAdServerReceiver microraidenAdServerReceiver;

    public MicroraidenController(MicroraidenAdServerSender microraidenAdServerSender, MicroraidenAdServerReceiver microraidenAdServerReceiver) {
        this.microraidenAdServerSender = microraidenAdServerSender;
        this.microraidenAdServerReceiver = microraidenAdServerReceiver;
    }

    @GetMapping("/microraiden/channel/create/{receiverAccountId}")
    public ChannelState createChannelToPublisher(@PathVariable String receiverAccountId) {
        return microraidenAdServerSender.createChannel(receiverAccountId);
    }

    @GetMapping("/microraiden/channel/create")
    public void createChannelsToDSP() {
        microraidenAdServerReceiver.createChannelsToDsp();
    }

    @GetMapping("/microraiden/channel/closeAll")
    public void closeAllChannels() {
        microraidenAdServerReceiver.closeAllChannels();
    }
}
