package com.peplatform.portfolioservice.common.constants;

/**
 * <p>Error codes utility class</p>
 * <p> provides constant values for all error codes used in the system.</p>
 *
 * @author Randjith
 * @version 1.0.0
 * @email "mailto:randjithkumar@no-reply@github.com"
 * @date 2026.07.11
 * @since 0.0.1-SNAPSHOT
 */
public final class ErrorCodes {

    /**
     * A constant representing the error code for a resource not found. This field is typically used to indicate that an attempted operation on a resource failed because such a resource does not exist.
     * <p>When this value occurs, the software should return an appropriate response or take the necessary corrective action to handle the situation gracefully.</p>
     */
    public static final String RESOURCE_NOT_FOUND = "RESOURCE_NOT_FOUND";
    /**
     * Indicates that a duplicate resource with the same name already exists.
     */
    public static final String DUPLICATE_RESOURCE = "DUPLICATE_RESOURCE";
    /**
     * Describes a business rule that is considered unsatisfactory.
     */
    public static final String BUSINESS_RULE = "BUSINESS_RULE";
    /**
     * Invalid or malformed data.
     * <p>Indicates that the input provided to a method did not pass basic validations,
     * possibly due to a user error, programming error, or external source (e.g., HTTP request).
     *
     * @see # BUSINESS_RULE
     * @see #VALIDATION_FAILED
     * @see #RESOURCE_NOT_FOUND
     */
    public static final String VALIDATION_FAILED = "VALIDATION_FAILED";
    /**
     * Access code block that identifies a concurrent error.
     */
    public static final String CONCURRENT_UPDATE = "CONCURRENT_UPDATE";
    /**
     * Describes the internal server error code.
     * This constant is used to represent a system failure that cannot be corrected by the application.
     */
    public static final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";

    /**
     * Constructs an instance of `ErrorCodes`.
     * <p>
     * This method is typically used to initialize the class fields.
     */
    private ErrorCodes() {
    }
}