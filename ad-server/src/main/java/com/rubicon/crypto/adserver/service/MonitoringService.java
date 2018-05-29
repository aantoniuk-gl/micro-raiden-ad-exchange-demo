package com.rubicon.crypto.adserver.service;

import com.rubicon.crypto.monitoring.LogEvent;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MonitoringService {

    private final RestTemplate restTemplate;
    private final String monitoringUri;
    private final String systemName;

    public MonitoringService(
            RestTemplate restTemplate,
            @Value("${service.monitoring.uri}") String monitoringUri,
            @Value("${spring.application.name}") String appName) {
        this.restTemplate = restTemplate;
        this.monitoringUri = monitoringUri;
        this.systemName = appName;
    }

    public void log(String msg) {
        LogEvent logEvent = new LogEvent(systemName, msg);

        HttpEntity<LogEvent> request = new HttpEntity<>(logEvent);
        restTemplate.postForEntity(monitoringUri + "/log", request, String.class);
    }
}
