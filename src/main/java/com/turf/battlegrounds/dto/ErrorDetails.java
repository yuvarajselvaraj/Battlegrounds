package com.turf.battlegrounds.dto;

public class ErrorDetails {
    private final String timestamp;
    private final int status;
    private final String error;
    private final String path;

    public ErrorDetails(String timestamp, int status, String error, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.path = path;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getPath() {
        return path;
    }
}
