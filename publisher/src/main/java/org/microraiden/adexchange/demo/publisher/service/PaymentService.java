package org.microraiden.adexchange.demo.publisher.service;

import org.microraiden.adexchange.demo.monitoring.ChannelState;
import org.microraiden.adexchange.demo.publisher.service.microraiden.BalanceProofStore;
import org.microraiden.adexchange.demo.publisher.service.microraiden.MicroraidenPublisherReceiver;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final MicroraidenPublisherReceiver publisherReceiver;
    private final BalanceProofStore balanceProofStore;
    private final MonitoringService monitoringService;

    public PaymentService(
            MicroraidenPublisherReceiver publisherReceiver,
            BalanceProofStore balanceProofStore,
            MonitoringService monitoringService) {
        this.publisherReceiver = publisherReceiver;
        this.balanceProofStore = balanceProofStore;
        this.monitoringService = monitoringService;
    }

    public void pay(ChannelState channelState) {
        // calculate payment's price
        double price = channelState.getBalance() - balanceProofStore.getBalanceProof(channelState.getSenderId()).getBalance();

        // update balance proof of Publisher from Ad-Exchange
        publisherReceiver.saveBalanceProof(channelState);

        monitoringService.logBusiness("Received " + price + " TKN from Ad-Exchange");
        monitoringService.logBusiness("-------------------------------------------------------");
    }
}
