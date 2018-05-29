package com.rubicon.crypto.dsp.web;

import com.rubicon.crypto.dsp.service.MonitoringService;
import com.rubicon.crypto.dsp.service.NotificationStorage;

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
