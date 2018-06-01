package org.microraiden.adexchange.demo.adserver.web;

import org.microraiden.adexchange.demo.adserver.service.PaymentService;
import org.microraiden.adexchange.demo.monitoring.ChannelState;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/pay")
    public void pay(@RequestBody ChannelState channelState) {
        paymentService.pay(channelState);
    }
}
