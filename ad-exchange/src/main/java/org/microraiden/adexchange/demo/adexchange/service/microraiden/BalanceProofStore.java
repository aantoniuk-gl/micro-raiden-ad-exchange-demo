package org.microraiden.adexchange.demo.adexchange.service.microraiden;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.microraiden.adexchange.demo.monitoring.ChannelState;
import org.springframework.stereotype.Service;

@Service
public class BalanceProofStore {

    private final ConcurrentHashMap<String, ChannelState> balanceProofStore = new ConcurrentHashMap<>();

    public ChannelState getBalanceProof(String senderId) {
        return balanceProofStore.get(senderId);
    }

    public Set<Map.Entry<String, ChannelState>> getAllBalanceProof() {
        return balanceProofStore.entrySet();
    }

    public void putBalanceProof(String senderId, ChannelState channelState) {
        balanceProofStore.put(senderId, channelState);
    }

    public void removeBalanceProof(String senderId) {
        balanceProofStore.remove(senderId);
    }
}
