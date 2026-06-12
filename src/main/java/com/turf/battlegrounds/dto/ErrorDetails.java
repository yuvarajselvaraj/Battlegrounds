package com.turf.battlegrounds.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Error details included in failed API responses")
public class ErrorDetails {
    @Schema(description = "ISO-8601 timestamp of the error", example = "2026-06-12T10:30:00.512345678Z")
    private final String timestamp;
    @Schema(description = "HTTP status code", example = "404")
    private final int status;
    @Schema(description = "HTTP status reason phrase", example = "Not Found")
    private final String error;
    @Schema(description = "Request path that caused the error", example = "/api/v1/users/99")
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
