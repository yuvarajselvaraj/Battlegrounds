package com.turf.battlegrounds.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(
        description = "Request body for creating a new user",
        example = """
                {
                  "username": "johndoe",
                  "email": "john@example.com",
                  "phone_no": "9876543210",
                  "password": "Password1!"
                }
                """
)
public class UserRequestDTO {
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    @Schema(description = "Unique username", example = "johndoe", minLength = 3, maxLength = 20)
    private String username;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    @Schema(description = "User email address", example = "john@example.com")
    private String email;

    @NotBlank(message = "Phone Number cannot be blank")
    @Schema(description = "Contact phone number", example = "9876543210")
    private String phone_no;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 20,
            message = "Password must be between 8 and 20 characters")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).*$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character"
    )
    @Schema(description = "Password (8-20 chars, must include upper, lower, digit, and special char)", example = "Password1!", minLength = 8, maxLength = 20)
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
