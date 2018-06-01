package org.microraiden.adexchange.demo.dsp.service.microraiden;

import java.math.BigInteger;

import javax.annotation.PostConstruct;

import org.apache.commons.codec.DecoderException;
import org.microraiden.MessageSigner;
import org.microraiden.Token;
import org.microraiden.TransferChannel;
import org.microraiden.Wallet;
import org.microraiden.adexchange.demo.dsp.service.MonitoringService;
import org.microraiden.adexchange.demo.monitoring.ChannelState;
import org.microraiden.conf.Configuration;
import org.microraiden.utils.Http;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MicroraidenDSPSender {

    private final MonitoringService monitoringService;
    private final Configuration configuration;
    private final String senderPrimaryKey;
    private final String deposit;
    private final String appName;

    private Wallet senderWallet;
    private String receiverAccountId;
    private String blockNumber;
    private double channelBalance;
    private TransferChannel transferChannel;
    private MessageSigner messageSigner;

    public MicroraidenDSPSender(
            MonitoringService monitoringService,
            Configuration configuration,
            @Value("${ethereum.account.primaryKey}") String senderPrimaryKey,
            @Value("${channel.deposit}") String deposit,
            @Value("${spring.application.name}") String appName) {
        this.monitoringService = monitoringService;
        this.configuration = configuration;
        this.senderPrimaryKey = senderPrimaryKey;
        this.deposit = deposit;
        this.appName = appName;
    }

    @PostConstruct
    private void init() throws DecoderException {
        senderWallet = new Wallet(senderPrimaryKey);

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

    public ChannelState createChannel(String receiverAccountId) {
        String blockNumber = transferChannel.createChannel(senderWallet, receiverAccountId, deposit);

        this.receiverAccountId = receiverAccountId;
        this.blockNumber = blockNumber;
        this.channelBalance = 0;

        String logMsg = appName + "(" + senderWallet.getAccountID() +
                ") created a transfer channel to Ad-Server(" + receiverAccountId +
                ") with deposit=" + deposit +
                " in blockNumber=" + blockNumber;
        monitoringService.log(logMsg);
        monitoringService.logBusiness(logMsg);

        byte[] balanceProof = getBalanceProof(blockNumber, channelBalance);

        return new ChannelState(
                senderWallet.getAccountID(),
                receiverAccountId,
                blockNumber,
                channelBalance,
                balanceProof);
    }

    public Double getChannelBalance() {
        return channelBalance;
    }

    public void updateChannelBalance(Double balance) {
        channelBalance = balance;
    }

    public ChannelState getChannelState(double balance) {
        byte[] balanceProof = getBalanceProof(blockNumber, balance);

        return new ChannelState(
                senderWallet.getAccountID(),
                receiverAccountId,
                blockNumber,
                balance,
                balanceProof);
    }

    private byte[] getBalanceProof(String blockNumber, double balance) {
        byte[] balanceProof = messageSigner.genBalanceMsgHashSig(
                senderWallet,
                receiverAccountId,
                configuration.getChannelAddr(),
                blockNumber,
                String.valueOf(balance));
        monitoringService.log(
                "New balance proof has been created between sender=" + senderWallet.getAccountID() +
                        " and receiver=" + receiverAccountId +
                        " with new balance=" + balance +
                        " in blockNumber=" + blockNumber);

        return balanceProof;
    }
}
