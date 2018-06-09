package org.microraiden.adexchange.demo.adexchange.service;

import org.microraiden.adexchange.demo.adexchange.service.microraiden.MicroraidenAdExchangeReceiver;
import org.microraiden.adexchange.demo.adexchange.service.microraiden.MicroraidenAdExchangeSender;
import org.microraiden.adexchange.demo.monitoring.ChannelState;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final MonitoringService monitoringService;
    private final MicroraidenAdExchangeReceiver adExchangeReceiver;
    private final MicroraidenAdExchangeSender adExchangeSender;
    private final PublisherService publisherReceiver;
    private final int fee;

    public PaymentService(
            MonitoringService monitoringService,
            MicroraidenAdExchangeReceiver adExchangeReceiver,
            MicroraidenAdExchangeSender adExchangeSender,
            PublisherService publisherReceiver,
            @Value("${ad-exchange.fee}") int fee) {
        this.monitoringService = monitoringService;
        this.adExchangeReceiver = adExchangeReceiver;
        this.adExchangeSender = adExchangeSender;
        this.publisherReceiver = publisherReceiver;
        this.fee = fee;
    }

    public void pay(ChannelState dspToAdExchangeChannelState) {
        // calculate payment's price
        double price = dspToAdExchangeChannelState.getBalance() -
                adExchangeReceiver.getChannelBalance(dspToAdExchangeChannelState.getBlockNumber());
        // update balance proof of Ad-Exchange from DSP
        adExchangeReceiver.saveBalanceProof(dspToAdExchangeChannelState);

        monitoringService.logBusiness("Received " + price + " TKN from DSP");

        // generate and send balance proof to publisher
        double newPrice = price - fee;
        double newBalance = adExchangeSender.getChannelBalance() + newPrice;

        ChannelState adExchangeToPublisherChannelState = adExchangeSender.getChannelState(newBalance);

        monitoringService.logBusiness("Got a fee in " + fee + " TNK");
        monitoringService.logBusiness("Sent " + newPrice + " TKN to Publisher");
        publisherReceiver.pay(adExchangeToPublisherChannelState);

        adExchangeSender.updateChannelBalance(newBalance);
    }
}
