package com.turf.battlegrounds.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(
        description = "Login credentials",
        example = """
                {
                  "username": "johndoe",
                  "password": "Password1!"
                }
                """
)
public class LoginRequest {
    @NotBlank
    @Schema(description = "Registered username", example = "johndoe")
    private String username;
    @NotBlank
    @Schema(description = "Account password", example = "Password1!")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
