package org.microraiden.adexchange.demo.monitoring.log;

import org.microraiden.adexchange.demo.monitoring.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LogToFileService implements LogService {

    private static final Logger LOGGER = LoggerFactory.getLogger("logger");
    private static final Logger BUSINESS_LOGGER = LoggerFactory.getLogger("businessLogger");

    @Override
    public void log(String system, String message) {
        LOGGER.info("{}: \"{}\"", system, message);
    }

    @Override
    public void logBusiness(String system, String message) {
        String msg = String.format("%10s: %s", system, message);
        BUSINESS_LOGGER.info(msg);
    }
}
