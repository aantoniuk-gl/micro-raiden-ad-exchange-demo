package org.microraiden.adexchange.demo.adserver.service.microraiden;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.microraiden.adexchange.demo.monitoring.ChannelState;
import org.springframework.stereotype.Service;

@Service
public class BalanceProofStore {

    private final ConcurrentHashMap<String, ChannelState> balanceProofStore = new ConcurrentHashMap<>();

    public ChannelState getBalanceProof(String blockNumber) {
        return balanceProofStore.get(blockNumber);
    }

    public Set<Map.Entry<String, ChannelState>> getAllBalanceProof() {
        return balanceProofStore.entrySet();
    }

    public void putBalanceProof(String blockNumber, ChannelState channelState) {
        balanceProofStore.put(blockNumber, channelState);
    }

    public void removeBalanceProof(String blockNumber) {
        balanceProofStore.remove(blockNumber);
    }
}
