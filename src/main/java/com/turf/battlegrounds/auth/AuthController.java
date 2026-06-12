package com.turf.battlegrounds.auth;

import com.turf.battlegrounds.dto.ApiResponse;
import com.turf.battlegrounds.openapi.OpenApiSchemas;
import com.turf.battlegrounds.user.UserRequestDTO;
import com.turf.battlegrounds.user.UserResponseDto;
import com.turf.battlegrounds.user.UserService;
import com.turf.battlegrounds.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Tag(name = "Authentication", description = "Public endpoints for signup, login, and token refresh")
@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Operation(summary = "Register a new user", description = "Creates a new user account and returns the created user profile.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "User created successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = OpenApiSchemas.SignupResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "422",
                    description = "Validation failed",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = OpenApiSchemas.ValidationErrorResponse.class)))
    })
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserResponseDto>> signup(@Valid @RequestBody UserRequestDTO req) {
        UserResponseDto created = userService.createUser(req);
        ApiResponse<UserResponseDto> body = new ApiResponse<>(201, "success", "User created", created);
        return ResponseEntity.status(201).body(body);
    }

    @Operation(summary = "Authenticate user", description = "Validates credentials and returns JWT access and refresh tokens.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Authentication successful",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = OpenApiSchemas.LoginResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Invalid credentials",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = OpenApiSchemas.InvalidCredentialsResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "422",
                    description = "Validation failed",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = OpenApiSchemas.ValidationErrorResponse.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
        String access = tokenProvider.generateAccessToken(userDetails);
        String refresh = tokenProvider.generateRefreshToken(userDetails);
        AuthResponse tokens = new AuthResponse(access, refresh);
        ApiResponse<AuthResponse> body = new ApiResponse<>(200, "success", "Authenticated", tokens);
        return ResponseEntity.ok(body);
    }

    @Operation(summary = "Refresh access token", description = "Issues new access and refresh tokens using a valid refresh token.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Token refreshed successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = OpenApiSchemas.RefreshTokenResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Invalid or expired refresh token",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = OpenApiSchemas.InvalidRefreshTokenResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "422",
                    description = "Validation failed",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = OpenApiSchemas.ValidationErrorResponse.class)))
    })
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(@Valid @RequestBody RefreshRequest req) {
        String refreshToken = req.getRefreshToken();
        if (!tokenProvider.validateToken(refreshToken)) {
            ApiResponse<AuthResponse> body = new ApiResponse<>(401, "error", "Invalid refresh token", null);
            return ResponseEntity.status(401).body(body);
        }
        String username = tokenProvider.getUsernameFromToken(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String access = tokenProvider.generateAccessToken(userDetails);
        String refresh = tokenProvider.generateRefreshToken(userDetails);
        AuthResponse tokens = new AuthResponse(access, refresh);
        ApiResponse<AuthResponse> body = new ApiResponse<>(200, "success", "Token refreshed", tokens);
        return ResponseEntity.ok(body);
    }
}
