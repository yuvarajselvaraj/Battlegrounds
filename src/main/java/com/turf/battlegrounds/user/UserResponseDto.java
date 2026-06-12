package com.turf.battlegrounds.user;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "User profile returned by the API",
        example = """
                {
                  "id": 1,
                  "name": "johndoe",
                  "email": "john@example.com"
                }
                """
)
public class UserResponseDto {

    @Schema(description = "Unique user ID", example = "1")
    private Long id;
    @Schema(description = "Username (mapped from the registered username)", example = "johndoe")
    private String name;
    @Schema(description = "Email address", example = "john@example.com")
    private String email;

    public UserResponseDto(
            Long id,
            String name,
            String email
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
