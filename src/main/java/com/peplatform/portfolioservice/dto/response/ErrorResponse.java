package com.peplatform.portfolioservice.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Error response model used for HTTP responses.
 *
 * <p>This class represents an error response with details like the overall success status, HTTP status code,
 * error code, message, path, and optional field-based errors. It is commonly used in web services to provide
 * comprehensive error information to clients.</p>
 *
 * @author Randjith
 * @version 1.0.0
 * @email mailto:randjithkumar@no-reply@github.com
 * @date 2026.07.11
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Builder
public class ErrorResponse {

    /**
     * Indicates whether the operation was successful.
     */
    private boolean success;

    /**
     * HTTP status code of the error response.
     */
    private int status;

    /**
     * Error code for the response.
     *
     * @see ErrorResponse
     */
    private String errorCode;

    /**
     * Error message describing the issue.
     */
    private String message;

    /**
     * Path of the request that caused the error
     */
    private String path;

    /**
     * Timestamp of the operation, generated automatically when an instance is created.
     *
     * @see LocalDateTime to obtain date and time information
     */
    private LocalDateTime timestamp;

    /**
     * A list of errors encountered during processing specific fields.
     * Each element contains details about a particular error field:
     *
     * @see FieldErrorResponse
     */
    private List<FieldErrorResponse> fieldErrors;

}