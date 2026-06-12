package com.turf.battlegrounds.auth;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "JWT token pair returned after successful authentication",
        example = """
                {
                  "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huZG9lIiwiaWF0IjoxNzgxMjYxOTUzLCJleHAiOjE3ODEyNjI4NTN9.example",
                  "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huZG9lIiwiaWF0IjoxNzgxMjYxOTUzLCJleHAiOjE3ODE4NjY3NTMsInR5cGUiOiJyZWZyZXNoIn0.example"
                }
                """
)
public class AuthResponse {
    @Schema(
            description = "Short-lived JWT access token for API requests",
            example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huZG9lIiwiaWF0IjoxNzgxMjYxOTUzLCJleHAiOjE3ODEyNjI4NTN9.example"
    )
    private String accessToken;
    @Schema(
            description = "Long-lived JWT refresh token used to obtain new access tokens",
            example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huZG9lIiwiaWF0IjoxNzgxMjYxOTUzLCJleHAiOjE3ODE4NjY3NTMsInR5cGUiOiJyZWZyZXNoIn0.example"
    )
    private String refreshToken;

    public AuthResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
