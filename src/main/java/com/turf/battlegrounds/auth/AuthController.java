package com.turf.battlegrounds.auth;

import com.turf.battlegrounds.dto.ApiResponse;
import com.turf.battlegrounds.user.UserRequestDTO;
import com.turf.battlegrounds.user.UserResponseDto;
import com.turf.battlegrounds.user.UserService;
import com.turf.battlegrounds.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserResponseDto>> signup(@Valid @RequestBody UserRequestDTO req) {
        // createUser will encode password already in current service; ensure no double-encode later
        UserResponseDto created = userService.createUser(req);
        ApiResponse<UserResponseDto> body = new ApiResponse<>(201, "success", "User created", created);
        return ResponseEntity.status(201).body(body);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        // load user details
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
        String access = tokenProvider.generateAccessToken(userDetails);
        String refresh = tokenProvider.generateRefreshToken(userDetails);
        AuthResponse tokens = new AuthResponse(access, refresh);
        ApiResponse<AuthResponse> body = new ApiResponse<>(200, "success", "Authenticated", tokens);
        return ResponseEntity.ok(body);
    }

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
