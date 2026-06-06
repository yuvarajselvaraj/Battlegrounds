package com.turf.battlegrounds.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turf.battlegrounds.dto.ApiResponse;
import com.turf.battlegrounds.dto.ErrorDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {
    private static final Logger log = LoggerFactory.getLogger(RestAccessDeniedHandler.class);
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.warn("Access denied: {} {}", request.getMethod(), request.getRequestURI());
        ErrorDetails details = new ErrorDetails(
                ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.getReasonPhrase(),
                request.getRequestURI()
        );
        ApiResponse<ErrorDetails> body = new ApiResponse<>(HttpStatus.FORBIDDEN.value(), "error", "Forbidden", details);
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");
        mapper.writeValue(response.getWriter(), body);
    }
}
