package com.rubicon.crypto.adserver.service;

import com.rubicon.crypto.adserver.service.microraiden.MicroraidenAdServerReceiver;
import com.rubicon.crypto.adserver.service.microraiden.MicroraidenAdServerSender;
import com.rubicon.crypto.monitoring.ChannelState;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    private final MicroraidenAdServerReceiver adServerReceiver;
    private final MicroraidenAdServerSender adServerSender;
    private final PublisherService publisherReceiver;
    private final int fee;

    public PaymentService(
            MicroraidenAdServerReceiver adServerReceiver,
            MicroraidenAdServerSender adServerSender,
            PublisherService publisherReceiver,
            @Value("${ad-server.fee}") int fee) {
        this.adServerReceiver = adServerReceiver;
        this.adServerSender = adServerSender;
        this.publisherReceiver = publisherReceiver;
        this.fee = fee;
    }

    public void pay(ChannelState dspToAdServerChannelState) {
        // calculate payment's price
        double price = dspToAdServerChannelState.getBalance() -
                adServerReceiver.getChannelBalance(dspToAdServerChannelState.getBlockNumber());
        // update balance proof from DSP
        adServerReceiver.saveBalanceProof(dspToAdServerChannelState);

        // generate and send balance proof to publisher
        double newBalance = adServerSender.getChannelBalance() + price - fee;
        ChannelState adServerToPublisherChannelState = adServerSender.getChannelState(newBalance);

        publisherReceiver.pay(adServerToPublisherChannelState);

        adServerSender.updateChannelBalance(newBalance);
    }
}
