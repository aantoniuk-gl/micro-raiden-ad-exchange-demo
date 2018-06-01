package org.microraiden.adexchange.demo.dsp.web;

import org.microraiden.adexchange.demo.dsp.service.MonitoringService;
import org.microraiden.adexchange.demo.dsp.service.NotificationStorage;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {

    private final NotificationStorage notificationStorage;
    private final MonitoringService monitoringService;

    public NotificationController(NotificationStorage notificationStorage, MonitoringService monitoringService) {
        this.notificationStorage = notificationStorage;
        this.monitoringService = monitoringService;
    }

    @GetMapping("/win/{impId}")
    public void win(@PathVariable String impId) {
        notificationStorage.addWinNotification(impId);

        monitoringService.log("Win Notification was received with impId=" + impId);
    }

    @GetMapping("/billing/{impId}")
    public void billing(@PathVariable String impId) {
        notificationStorage.addBillingNotification(impId);

        monitoringService.log("Billing Notification was received with impId=" + impId);
    }
}
