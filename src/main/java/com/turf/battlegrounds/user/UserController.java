package com.turf.battlegrounds.user;

import com.turf.battlegrounds.dto.ApiResponse;
import com.turf.battlegrounds.openapi.OpenApiSchemas;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Users", description = "User management endpoints (requires JWT authentication)")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @Operation(summary = "Get user by ID", description = "Returns a single user profile by their unique identifier.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "User found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = OpenApiSchemas.UserDetailResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Missing or invalid JWT",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = OpenApiSchemas.UnauthorizedResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = OpenApiSchemas.NotFoundResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUserDetail(
            @Parameter(description = "Unique user ID", example = "1") @PathVariable Long id) {
        UserResponseDto user = userService.getUser(id);
        ApiResponse<UserResponseDto> userResponse = new ApiResponse<>(200, "success", "User Detail fetched successfully", user);
        return ResponseEntity.ok(userResponse);
    }

    @Operation(summary = "List all users", description = "Returns a list of all registered users.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Users retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = OpenApiSchemas.UserListResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Missing or invalid JWT",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = OpenApiSchemas.UnauthorizedResponse.class)))
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> getUsers() {
        List<UserResponseDto> users = userService.getAllUsers();
        ApiResponse<List<UserResponseDto>> user = new ApiResponse<>(200, "success", "User Details Fetched Successfully", users);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Create a user", description = "Creates a new user account. Requires authentication.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "User created successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = OpenApiSchemas.UserCreateResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Missing or invalid JWT",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = OpenApiSchemas.UnauthorizedResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "422",
                    description = "Validation failed",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = OpenApiSchemas.ValidationErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<ApiResponse<UserResponseDto>> createUser(@Valid @RequestBody UserRequestDTO userRequest) {
        UserResponseDto user = userService.createUser(userRequest);
        ApiResponse<UserResponseDto> userResponse = new ApiResponse<>(201, "success", "User Created successfully", user);
        return ResponseEntity.ok(userResponse);
    }
}
