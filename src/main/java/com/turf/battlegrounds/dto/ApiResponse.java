package com.turf.battlegrounds.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Standard API response wrapper")
public class ApiResponse<T> {

    @Schema(description = "HTTP status code", example = "200")
    private int statusCode;
    @Schema(description = "Outcome status", example = "success", allowableValues = {"success", "error"})
    private String status;
    @Schema(description = "Human-readable message", example = "User Detail fetched successfully")
    private String message;
    @Schema(description = "Response payload")
    private T data;

    public ApiResponse(int statusCode, String status, String message, T data) {
        this.statusCode = statusCode;
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
