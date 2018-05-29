package com.rubicon.crypto.monitoring;

public class ChannelState {

    private String senderId;
    private String receiverId;
    private String blockNumber;
    private Double balance;
    private byte[] balanceProof;

    public ChannelState(String senderId, String receiverId, String blockNumber, Double balance, byte[] balanceProof) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.blockNumber = blockNumber;
        this.balance = balance;
        this.balanceProof = balanceProof;
    }

    public ChannelState() {
    }

    public String getSenderId() {
        return senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getBlockNumber() {
        return blockNumber;
    }

    public Double getBalance() {
        return balance;
    }

    public byte[] getBalanceProof() {
        return balanceProof;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public void setBlockNumber(String blockNumber) {
        this.blockNumber = blockNumber;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public void setBalanceProof(byte[] balanceProof) {
        this.balanceProof = balanceProof;
    }
}
