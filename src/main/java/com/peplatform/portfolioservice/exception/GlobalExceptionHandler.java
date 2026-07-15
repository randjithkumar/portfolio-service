package com.peplatform.portfolioservice.exception;

import com.peplatform.portfolioservice.dto.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Global exception handler class
 * <p>Handles various types of exceptions that occur within the application and provides appropriate error responses
 *
 * @author Randjith
 * @version 1.0.0
 * @email "mailto:randjithkumar@no-reply@github.com"
 * @date 2026.07.10
 * @since 0.0.1-SNAPSHOT
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
//
//    /**
//     * Handles {@link ResourceNotFoundException} and returns a 404 Not Found response.
//     *
//     * @param resourceNotFoundException the exception to handle
//     * @return a map containing error details
//     */
//    @ExceptionHandler(ResourceNotFoundException.class)
//    public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException resourceNotFoundException) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
//                Map.of(
//                        "timestamp", LocalDateTime.now(),
//                        "status", 404,
//                        "error", "Not Found",
//                        "message", resourceNotFoundException.getMessage()
//                )
//        );
//    }

    /**
     * Handles {@link DuplicateResourceException} and returns a 409 Conflict response.
     *
     * @param ex the exception to handle
     * @return a map containing validation error details
     */
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicate(DuplicateResourceException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", 409,
                        "error", "Conflict",
                        "message", ex.getMessage()
                )
        );
    }

    /**
     * Handles the {@link MethodArgumentNotValidException} and returns a 400 Bad Request response.
     *
     * @param ex The exception to handle
     * @return a map containing validation error details
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .orElse("Validation failed");

        return ResponseEntity.badRequest().body(
                Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", 400,
                        "error", "Bad Request",
                        "message", message
                )
        );
    }

    /**
     * Handles {@link BusinessRuleException} and returns a 409 Conflict response.
     *
     * @param exception the exception to handle
     * @return a map containing error details
     */
    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessRule(
            BusinessRuleException exception
    ) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_CONTENT)
                .body(
                        Map.of(
                                "timestamp", LocalDateTime.now(),
                                "status",
                                HttpStatus.UNPROCESSABLE_CONTENT.value(),
                                "error", "Business Rule Violation",
                                "message", exception.getMessage()
                        )
                );
    }

    /**
     * Handles the {@link ObjectOptimisticLockingFailureException} and returns a 409 Conflict response.
     *
     * @param ex The exception to handle
     * @return a map containing error details
     */
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<Map<String, Object>> handleOptimisticLocking(ObjectOptimisticLockingFailureException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(
                        Map.of(
                                "timestamp", LocalDateTime.now(),
                                "status", 409,
                                "error", "Concurrent Update",
                                "message",
                                "The record was modified by another transaction. Please refresh and retry."
                        )
                );
    }

    /**
     * Handles {@linkResourceNotFoundException} and returns a 404 Not Found response.
     *
     * @param resourceNotFoundException the exception to handle
     * @return a map containing error details
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
            ResourceNotFoundException resourceNotFoundException,
            HttpServletRequest request
    ) {

        ErrorResponse response = ErrorResponse.builder()
                .success(false)
                .status(HttpStatus.NOT_FOUND.value())
                .errorCode("RESOURCE_NOT_FOUND")
                .message(resourceNotFoundException.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    /**
     * Handles the {@link IllegalArgumentException} and returns a 400 Bad Request response.
     *
     * @param ex The exception to handle
     * @return a map containing validation error details
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.BAD_REQUEST.value(),
                "error", "Bad Request",
                "message", ex.getMessage()
        ));
    }

}