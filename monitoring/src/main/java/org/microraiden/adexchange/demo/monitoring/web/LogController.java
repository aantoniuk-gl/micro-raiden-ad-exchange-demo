package org.microraiden.adexchange.demo.monitoring.web;

import org.microraiden.adexchange.demo.monitoring.LogEvent;
import org.microraiden.adexchange.demo.monitoring.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogController {

    @Autowired
    private LogService logService;

    @PostMapping(path = "/log", consumes = "application/json")
    public void log(@RequestBody LogEvent logEvent) {
        logService.log(logEvent.getSystem(), logEvent.getMessage());
    }

    @PostMapping(path = "/log-business", consumes = "application/json")
    public void logBusiness(@RequestBody LogEvent logEvent) {
        logService.logBusiness(logEvent.getSystem(), logEvent.getMessage());
    }
}
