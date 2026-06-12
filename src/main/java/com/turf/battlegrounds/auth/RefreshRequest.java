package com.turf.battlegrounds.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(
        description = "Refresh token request",
        example = """
                {
                  "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huZG9lIiwiaWF0IjoxNzgxMjYxOTUzLCJleHAiOjE3ODE4NjY3NTMsInR5cGUiOiJyZWZyZXNoIn0.example"
                }
                """
)
public class RefreshRequest {
    @NotBlank
    @Schema(
            description = "Valid refresh token from a previous login or refresh response",
            example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huZG9lIiwiaWF0IjoxNzgxMjYxOTUzLCJleHAiOjE3ODE4NjY3NTMsInR5cGUiOiJyZWZyZXNoIn0.example"
    )
    private String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
