package com.peplatform.portfolioservice.common.util;

import com.peplatform.portfolioservice.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * ApiResponseBuilder class provides a builder pattern for creating ApiResponse objects.
 * It is used in the apiresponsebuilder component to encapsulate all necessary data such as success status,
 * HTTP status code, message, and timestamp.
 *
 * @author Randjith
 * @version 1.0.0
 * @apiresponsebuilder Business Logic Layer
 * <p>Provides business logic processing related to response building in the apiresponsebuilder component,
 * allowing the creation of ApiResponse objects based on various success or error scenarios.
 * @email "mailto:randjithkumar@no-reply@github.com"
 * @date 2026.07.11
 * @since 0.0.1-SNAPSHOT
 */
public final class ApiResponseBuilder {

    /**
     * Class for building API responses.
     */
    private ApiResponseBuilder() {
    }

    /**
     * Creates a new response body with the given success status, message, and optional data.
     *
     * @param status  The HTTP status code of the response.
     * @param message The message associated with the response.
     * @param data    Optional data to be included in the response body.
     * @return An ApiResponse object encapsulating the specified data and properties.
     */
    public static <T> ApiResponse<T> success(
            HttpStatus status,
            String message,
            T data
    ) {

        return ApiResponse.<T>builder()
                .success(true)
                .status(status.value())
                .message(message)
                .timestamp(LocalDateTime.now())
                .data(data)
                .build();
    }

}