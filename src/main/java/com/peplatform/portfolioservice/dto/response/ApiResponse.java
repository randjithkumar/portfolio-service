/**
 * Constructs a builder for an ApiResponse object.
 *
 * @return A new builder initialized with default values.
 */
package com.peplatform.portfolioservice.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Service layer for user-related operations. It provides methods to create, update, query, and delete users.
 *
 * @author Randjith
 * @version 1.0.0
 * @email mailto:randjithkumar@no-reply@github.com
 * @date 2026.07.11
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Builder
public class ApiResponse<T> {

    /**
     * Indicates whether the API response was successful.
     */
    private boolean success;

    /**
     * HTTP status code of the response.
     */
    private int status;

    /**
     * Response message
     */
    private String message;

    /**
     * Timestamp of the response creation.
     */
    private LocalDateTime timestamp;

    /**
     * Response data returned by an HTTP API call. Each request might have a variety of response formats, and this type is designed to accommodate different response body structures.
     * <p>
     * The field `data` contains the actual response content, which varies depending on the request type (e.g., JSON, XML, plain text). This field automatically populates when data is retrieved from the API response due to the constructor initialization logic provided by Jackson annotations. Any type of object can be returned as long as it implements Jackson's `@JsonDeserialize` annotation.
     *
     */
    private T data;

}