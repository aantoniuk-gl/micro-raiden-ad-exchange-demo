package org.microraiden.adexchange.demo.adexchange.web;

import org.microraiden.adexchange.demo.adexchange.service.microraiden.MicroraidenAdExchangeReceiver;
import org.microraiden.adexchange.demo.adexchange.service.microraiden.MicroraidenAdExchangeSender;
import org.microraiden.adexchange.demo.monitoring.ChannelState;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MicroraidenController {

    private final MicroraidenAdExchangeSender microraidenAdExchangeSender;
    private final MicroraidenAdExchangeReceiver microraidenAdExchangeReceiver;

    public MicroraidenController(MicroraidenAdExchangeSender microraidenAdExchangeSender, MicroraidenAdExchangeReceiver microraidenAdExchangeReceiver) {
        this.microraidenAdExchangeSender = microraidenAdExchangeSender;
        this.microraidenAdExchangeReceiver = microraidenAdExchangeReceiver;
    }

    @GetMapping("/microraiden/channel/create/{receiverAccountId}")
    public ChannelState createChannelToPublisher(@PathVariable String receiverAccountId) {
        return microraidenAdExchangeSender.createChannel(receiverAccountId);
    }

    @GetMapping("/microraiden/channel/create")
    public void createChannelsToDSP() {
        microraidenAdExchangeReceiver.createChannelsToDsp();
    }

    @GetMapping("/microraiden/channel/closeAll")
    public void closeAllChannels() {
        microraidenAdExchangeReceiver.closeAllChannels();
    }
}
