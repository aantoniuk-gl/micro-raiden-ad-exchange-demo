package org.microraiden.adexchange.demo.publisher.web;

import org.microraiden.adexchange.demo.monitoring.ChannelState;
import org.microraiden.adexchange.demo.publisher.service.PaymentService;
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
