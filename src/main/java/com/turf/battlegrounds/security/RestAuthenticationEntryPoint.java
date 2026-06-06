package com.turf.battlegrounds.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turf.battlegrounds.dto.ApiResponse;
import com.turf.battlegrounds.dto.ErrorDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static final Logger log = LoggerFactory.getLogger(RestAuthenticationEntryPoint.class);
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.warn("Unauthorized request: {} {}", request.getMethod(), request.getRequestURI());
        ErrorDetails details = new ErrorDetails(
                ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                request.getRequestURI()
        );
        String msg = authException.getMessage() == null ? "Unauthorized" : authException.getMessage();
        ApiResponse<ErrorDetails> body = new ApiResponse<>(HttpStatus.UNAUTHORIZED.value(), "error", msg, details);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        mapper.writeValue(response.getWriter(), body);
    }
}
