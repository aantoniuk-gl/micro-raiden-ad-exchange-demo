package org.microraiden.adexchange.demo.dsp.service;

import java.util.concurrent.ConcurrentHashMap;

import com.google.openrtb.OpenRtb.BidResponse.SeatBid.Bid;

import org.springframework.stereotype.Service;

@Service
public class ImpressionStorage {

    private static final ConcurrentHashMap<String, Double> impressions = new ConcurrentHashMap<>();

    public void addImpression(String impId, Double price) {
        impressions.put(impId, price);
    }

    public Double getImpression(String impId) {
        return impressions.get(impId);
    }
}
