package org.microraiden.adexchange.demo.dsp.web;

import com.google.openrtb.OpenRtb.BidResponse.SeatBid.Bid;
import org.microraiden.adexchange.demo.dsp.service.ImpressionStorage;
import org.microraiden.adexchange.demo.dsp.service.MonitoringService;
import org.microraiden.adexchange.demo.dsp.service.ResponseBuilder;

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
