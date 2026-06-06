package com.turf.battlegrounds.exception;

import com.turf.battlegrounds.dto.ApiResponse;
import com.turf.battlegrounds.dto.ErrorDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.http.converter.HttpMessageNotReadableException;

import jakarta.servlet.http.HttpServletRequest;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<ErrorDetails>> handleNotFound(UserNotFoundException ex, HttpServletRequest req) {
        log.warn("Resource not found: {}", ex.getMessage());
        ErrorDetails details = new ErrorDetails(
                ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                req.getRequestURI()
        );
        ApiResponse<ErrorDetails> body = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "error", ex.getMessage(), details);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<ErrorDetails>> handleNoHandlerFound(NoHandlerFoundException ex, HttpServletRequest req) {
        log.warn("Page not found: {} {}", req.getMethod(), req.getRequestURI());
        ErrorDetails details = new ErrorDetails(
                ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                req.getRequestURI()
        );
        String message = "Page not found";
        ApiResponse<ErrorDetails> body = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "error", message, details);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<ErrorDetails>> handleValidation(org.springframework.web.bind.MethodArgumentNotValidException ex, HttpServletRequest req) {
        log.warn("Validation failed: {}", ex.getMessage());
        java.util.List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(java.util.stream.Collectors.toList());
        String message = errors.isEmpty() ? "Validation failed" : String.join("; ", errors);
        ErrorDetails details = new ErrorDetails(
                ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                req.getRequestURI()
        );
        ApiResponse<ErrorDetails> body = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "error", message, details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<ErrorDetails>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpServletRequest req) {
        log.warn("Request body not readable: {}", ex.getMessage());
        String msg = ex.getMessage();
        if (msg == null || msg.contains("Required request body is missing")) {
            msg = "Required request body is missing";
        }
        ErrorDetails details = new ErrorDetails(
                ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                req.getRequestURI()
        );
        ApiResponse<ErrorDetails> body = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "error", msg, details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<ErrorDetails>> handleGeneric(Exception ex, HttpServletRequest req) {
        log.error("Unhandled exception", ex);
        ErrorDetails details = new ErrorDetails(
                ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                req.getRequestURI()
        );
        ApiResponse<ErrorDetails> body = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "error", "An unexpected error occurred", details);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
