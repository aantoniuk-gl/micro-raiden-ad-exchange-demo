package org.microraiden.adexchange.demo.monitoring;

public interface LogService {
    void log(String system, String message);
    void logBusiness(String system, String message);
}
