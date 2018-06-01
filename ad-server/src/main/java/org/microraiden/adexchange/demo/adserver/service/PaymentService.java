package org.microraiden.adexchange.demo.adserver.service;

import org.microraiden.adexchange.demo.adserver.service.microraiden.MicroraidenAdServerReceiver;
import org.microraiden.adexchange.demo.adserver.service.microraiden.MicroraidenAdServerSender;
import org.microraiden.adexchange.demo.monitoring.ChannelState;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final MonitoringService monitoringService;
    private final MicroraidenAdServerReceiver adServerReceiver;
    private final MicroraidenAdServerSender adServerSender;
    private final PublisherService publisherReceiver;
    private final int fee;

    public PaymentService(
            MonitoringService monitoringService,
            MicroraidenAdServerReceiver adServerReceiver,
            MicroraidenAdServerSender adServerSender,
            PublisherService publisherReceiver,
            @Value("${ad-server.fee}") int fee) {
        this.monitoringService = monitoringService;
        this.adServerReceiver = adServerReceiver;
        this.adServerSender = adServerSender;
        this.publisherReceiver = publisherReceiver;
        this.fee = fee;
    }

    public void pay(ChannelState dspToAdServerChannelState) {
        // calculate payment's price
        double price = dspToAdServerChannelState.getBalance() -
                adServerReceiver.getChannelBalance(dspToAdServerChannelState.getBlockNumber());
        // update balance proof of Ad-Server from DSP
        adServerReceiver.saveBalanceProof(dspToAdServerChannelState);

        monitoringService.logBusiness("Received " + price + " TKN from DSP(" +
                dspToAdServerChannelState.getSenderId() + ")");

        // generate and send balance proof to publisher
        double newPrice = price - fee;
        double newBalance = adServerSender.getChannelBalance() + newPrice;

        ChannelState adServerToPublisherChannelState = adServerSender.getChannelState(newBalance);

        monitoringService.logBusiness("Sent " + newPrice + " TKN to Publisher");
        publisherReceiver.pay(adServerToPublisherChannelState);

        adServerSender.updateChannelBalance(newBalance);
    }
}
