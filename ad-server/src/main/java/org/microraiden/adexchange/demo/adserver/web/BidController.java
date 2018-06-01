package org.microraiden.adexchange.demo.adserver.web;

import com.google.openrtb.OpenRtb.BidResponse.SeatBid.Bid;
import org.microraiden.adexchange.demo.adserver.service.DspBidCollector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BidController {

    @Autowired
    private DspBidCollector dspBidCollector;

    @GetMapping("/bid")
    public Bid bid() {
        return dspBidCollector.collect();
    }

    @GetMapping("/win")
    public void win(@RequestParam String impId, @RequestParam String dspWinUrl) {
        dspBidCollector.sendWinNotification(impId, dspWinUrl);
    }
}
