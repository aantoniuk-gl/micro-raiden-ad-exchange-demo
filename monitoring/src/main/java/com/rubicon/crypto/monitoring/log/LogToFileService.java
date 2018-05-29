package com.rubicon.crypto.monitoring.log;

import com.rubicon.crypto.monitoring.LogService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LogToFileService implements LogService {
    private static final Logger LOGGER = LoggerFactory.getLogger("saveToFile");

    @Override
    public void log(String system, String message) {
        LOGGER.info("{}: \"{}\"", system, message);
    }
}
