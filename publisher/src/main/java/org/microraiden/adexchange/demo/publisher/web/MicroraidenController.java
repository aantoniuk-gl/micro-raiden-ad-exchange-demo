package org.microraiden.adexchange.demo.publisher.web;

import org.microraiden.adexchange.demo.publisher.service.microraiden.MicroraidenPublisherReceiver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MicroraidenController {

    @Autowired
    private MicroraidenPublisherReceiver microraidenPublisherReceiver;

    @GetMapping("/microraiden/channel/create")
    public void createChannels() {
        microraidenPublisherReceiver.createChannelsToAdService();
    }

    @GetMapping("/microraiden/channel/closeAll")
    public void closeAllChannels() {
        microraidenPublisherReceiver.closeAllChannels();
    }
}
