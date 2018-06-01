package org.microraiden.adexchange.demo.dsp.service;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationStorage {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationStorage.class);

    private static final Set<String> winNotificationStorage = new HashSet<>();
    private static final Set<String> billingNotificationStorage = new HashSet<>();

    public void addWinNotification(String impId) {
        winNotificationStorage.add(impId);
        LOGGER.debug("Win Notification was added with impId '{}'", impId);
    }

    public void addBillingNotification(String impId) {
        billingNotificationStorage.add(impId);
        LOGGER.debug("Billing Notification was added with impId '{}'", impId);
    }

    public void removeWinNotification(String impId) {
        winNotificationStorage.remove(impId);
        LOGGER.debug("Win Notification was removed with impId '{}'", impId);
    }

    public void removeBillingNotification(String impId) {
        billingNotificationStorage.remove(impId);
        LOGGER.debug("Billing Notification was removed with impId '{}'", impId);
    }

    public Set<String> getAllWinNotifications() {
        return ImmutableSet.copyOf(winNotificationStorage);
    }

    public Set<String> getAllBillingNotifications() {
        return ImmutableSet.copyOf(billingNotificationStorage);
    }
}
