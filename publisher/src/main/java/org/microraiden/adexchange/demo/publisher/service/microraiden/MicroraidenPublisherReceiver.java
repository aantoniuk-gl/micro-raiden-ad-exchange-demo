package org.microraiden.adexchange.demo.publisher.service.microraiden;

import java.math.BigInteger;

import javax.annotation.PostConstruct;

import org.apache.commons.codec.DecoderException;
import org.microraiden.MessageSigner;
import org.microraiden.Token;
import org.microraiden.TransferChannel;
import org.microraiden.Wallet;
import org.microraiden.adexchange.demo.monitoring.ChannelState;
import org.microraiden.adexchange.demo.publisher.service.MonitoringService;
import org.microraiden.conf.Configuration;
import org.microraiden.utils.Http;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MicroraidenPublisherReceiver {

    private final MonitoringService monitoringService;
    private final BalanceProofStore balanceProofStore;
    private final MicroraidenAdExchangeSender microraidenAdExchangeSender;
    private final Configuration configuration;
    private final String receiverPrimaryKey;

    private Wallet receiverWallet;
    private TransferChannel transferChannel;
    private MessageSigner messageSigner;

    @Autowired
    public MicroraidenPublisherReceiver(
            MonitoringService monitoringService,
            BalanceProofStore balanceProofStore, MicroraidenAdExchangeSender microraidenAdExchangeSender, Configuration configuration,
            @Value("${ethereum.account.primaryKey}") String receiverPrimaryKey) {
        this.monitoringService = monitoringService;
        this.balanceProofStore = balanceProofStore;
        this.microraidenAdExchangeSender = microraidenAdExchangeSender;
        this.configuration = configuration;
        this.receiverPrimaryKey = receiverPrimaryKey;
    }

    @PostConstruct
    private void init() throws DecoderException {
        receiverWallet = new Wallet(receiverPrimaryKey);

        Http httpAgent = new Http(configuration.getRpcAddress(), configuration.isDebugInfo());

        Token token = new Token(
                configuration.getTokenABI(),
                configuration.getTokenAddr(),
                configuration.getAppendingZerosForTKN(),
                configuration.getAppendingZerosForETH(),
                configuration.getGasPrice(),
                httpAgent,
                configuration.isDebugInfo());

        BigInteger maxDeposit = new BigInteger("2", 10).pow(configuration.getMaxDepositBits());
        transferChannel = new TransferChannel(
                configuration.getChannelAddr(),
                configuration.getChannelABI(),
                maxDeposit,
                token,
                configuration.getGasPrice(),
                httpAgent,
                configuration.isDebugInfo());

        messageSigner = new MessageSigner(
                configuration.getAppendingZerosForTKN(),
                httpAgent,
                configuration.isDebugInfo());
    }

    public void createChannelsToAdService() {
        microraidenAdExchangeSender.createChannels(receiverWallet.getAccountID())
                                 .forEach(channel -> balanceProofStore.putBalanceProof(channel.getSenderId(), channel));
    }

    public void saveBalanceProof(ChannelState channelState) {
        balanceProofStore.putBalanceProof(channelState.getSenderId(), channelState);
    }

    public void closeAllChannels() {
        balanceProofStore.getAllBalanceProof().forEach(e -> closeChannel(e.getKey()));
    }

    private void closeChannel(String senderId) {
        ChannelState channelState = balanceProofStore.getBalanceProof(senderId);
        byte[] closingMsgHashSig = messageSigner.genClosingMsgHashSig(
                receiverWallet,
                channelState.getSenderId(),
                configuration.getChannelAddr(),
                channelState.getBlockNumber(),
                channelState.getBalance().toString());

        transferChannel.closeChannelCooperatively(
                receiverWallet,
                receiverWallet.getAccountID(),
                channelState.getBalanceProof(),
                closingMsgHashSig,
                channelState.getBlockNumber(),
                channelState.getBalance().toString());

        balanceProofStore.removeBalanceProof(senderId);

        String logMsg = "Ad-Exchange(" + channelState.getSenderId() +
                " closed a channel and sent " +
                channelState.getBalance() +
                " TKN to Publisher(" + receiverWallet.getAccountID() + ")";
        monitoringService.log(logMsg);
        monitoringService.logBusiness(logMsg);
    }
}
