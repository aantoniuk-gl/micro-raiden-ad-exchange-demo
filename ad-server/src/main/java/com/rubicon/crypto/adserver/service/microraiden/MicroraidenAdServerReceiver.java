package com.rubicon.crypto.adserver.service.microraiden;

import java.math.BigInteger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.rubicon.crypto.adserver.service.MonitoringService;
import com.rubicon.crypto.monitoring.ChannelState;

import org.apache.commons.codec.DecoderException;
import org.microraiden.MessageSigner;
import org.microraiden.Token;
import org.microraiden.TransferChannel;
import org.microraiden.Wallet;
import org.microraiden.conf.Configuration;
import org.microraiden.utils.Http;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class MicroraidenAdServerReceiver {

    private final MonitoringService monitoringService;
    private final BalanceProofStore balanceProofStore;
    private final MicroraidenDspSender microraidenDspSender;
    private final Configuration configuration;
    private final String receiverPrimaryKey;

    private Wallet receiverWallet;
    private TransferChannel transferChannel;
    private MessageSigner messageSigner;

    @Autowired
    public MicroraidenAdServerReceiver(
            MonitoringService monitoringService,
            BalanceProofStore balanceProofStore, MicroraidenDspSender microraidenDspSender, Configuration configuration,
            @Value("${ethereum.account.primaryKey}") String receiverPrimaryKey) {
        this.monitoringService = monitoringService;
        this.balanceProofStore = balanceProofStore;
        this.microraidenDspSender = microraidenDspSender;
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

    @Scheduled(initialDelay=5000, fixedDelay = Integer.MAX_VALUE)
    private void createChannels() {
        createChannelsToDsp();
    }

    @PreDestroy
    private void destroy() {
        closeAllChannels();
    }

    public void createChannelsToDsp() {
        microraidenDspSender.createChannels(receiverWallet.getAccountID())
                            .forEach(channel -> balanceProofStore.putBalanceProof(channel.getBlockNumber(), channel));
    }

    public double getChannelBalance(String blockNumber) {
        return balanceProofStore.getBalanceProof(blockNumber).getBalance();
    }

    public void saveBalanceProof(ChannelState channelState) {
        balanceProofStore.putBalanceProof(channelState.getBlockNumber(), channelState);
    }

    public void closeAllChannels() {
        balanceProofStore.getAllBalanceProof().forEach(e -> closeChannel(e.getKey()));
    }

    private void closeChannel(String blockNumber) {
        ChannelState channelState = balanceProofStore.getBalanceProof(blockNumber);
        byte[] closingMsgHashSig = messageSigner.genClosingMsgHashSig(
                receiverWallet,
                channelState.getSenderId(),
                configuration.getChannelAddr(),
                blockNumber,
                channelState.getBalance().toString());

        transferChannel.closeChannelCooperatively(
                receiverWallet,
                receiverWallet.getAccountID(),
                channelState.getBalanceProof(),
                closingMsgHashSig,
                blockNumber,
                channelState.getBalance().toString());

        balanceProofStore.removeBalanceProof(blockNumber);

        monitoringService.log(
                "Transfer channel has been closed between sender=" + channelState.getSenderId() +
                        " and receiver=" + receiverWallet.getAccountID() +
                        " with balance=" + channelState.getBalance() +
                        " in blockNumber=" + blockNumber);
    }
}
