package com.rubicon.crypto.dsp.web;

import com.google.openrtb.OpenRtb.BidResponse.SeatBid.Bid;
import com.rubicon.crypto.dsp.service.ImpressionStorage;
import com.rubicon.crypto.dsp.service.MonitoringService;
import com.rubicon.crypto.dsp.service.ResponseBuilder;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BidController {

    private final ResponseBuilder responseBuilder;
    private final MonitoringService monitoringService;
    private  final ImpressionStorage impressionStorage;

    public BidController(ResponseBuilder responseBuilder, MonitoringService monitoringService, ImpressionStorage impressionStorage) {
        this.responseBuilder = responseBuilder;
        this.monitoringService = monitoringService;
        this.impressionStorage = impressionStorage;
    }

    @GetMapping("/bid/{impId}")
    public Bid bid(@PathVariable String impId) {
        Bid bidResponse = responseBuilder.build(impId);

        impressionStorage.addImpression(impId, bidResponse.getPrice());

        monitoringService.log("Bid was created:\n" + bidResponse);

        return bidResponse;
    }
}
