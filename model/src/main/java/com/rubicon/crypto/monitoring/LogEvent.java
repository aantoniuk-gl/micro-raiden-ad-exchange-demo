package com.rubicon.crypto.monitoring;

public class LogEvent {
    private String system;
    private String message;

    public LogEvent(String system, String message) {
        this.system = system;
        this.message = message;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
