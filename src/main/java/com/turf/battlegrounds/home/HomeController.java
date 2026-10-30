package com.turf.battlegrounds.home;

import com.turf.battlegrounds.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Tag(name = "Home", description = "Public root endpoint")
@RestController
public class HomeController {

    private final String applicationName;

    public HomeController(@Value("${spring.application.name}") String applicationName) {
        this.applicationName = applicationName;
    }

    @Operation(summary = "API root", description = "Public welcome endpoint confirming the API is reachable.")
    @GetMapping("/")
    public ResponseEntity<ApiResponse<Map<String, String>>> home() {
        ApiResponse<Map<String, String>> body = new ApiResponse<>(200, "success", "Welcome to the " + applicationName + " API",
                Map.of("name", applicationName));
        return ResponseEntity.ok(body);
    }

    // Browsers request this automatically; answer with an empty response instead of a 401/404
    @Hidden
    @GetMapping("/favicon.ico")
    public ResponseEntity<Void> favicon() {
        return ResponseEntity.noContent().build();
    }
}
