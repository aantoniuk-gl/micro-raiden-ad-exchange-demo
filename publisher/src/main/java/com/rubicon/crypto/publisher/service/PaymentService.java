package com.rubicon.crypto.publisher.service;

import com.rubicon.crypto.monitoring.ChannelState;
import com.rubicon.crypto.publisher.service.microraiden.MicroraidenPublisherReceiver;

import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final MicroraidenPublisherReceiver publisherReceiver;

    public PaymentService(MicroraidenPublisherReceiver publisherReceiver) {
        this.publisherReceiver = publisherReceiver;
    }

    public void pay(ChannelState channelState) {
        publisherReceiver.saveBalanceProof(channelState);
    }
}
