package com.peplatform.portfolioservice.dto.response;

import lombok.Builder;
import lombok.Getter;

/**
 * The FieldErrorResponse class represents a data object that describes an error field associated with a request.
 * <p>
 * This class is used to encapsulate information about an erroneous field and its error message, which can be sent as part of a response in case of errors during form validation or processing of requests.
 *
 * @author Randjith
 * @version 1.0.0
 * @email mailto:randjithkumar@no-reply@github.com
 * @date 2026.07.11
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Builder
public class FieldErrorResponse {

    /**
     * User specific error handling configuration for various fields in the user registration process.
     * <p><strong>Example:</strong>
     * This property is used to specify custom error messages for fields that cannot be empty
     * when a new user is registered. The format of this message is similar to Spring Boot's
     * error message conventions, where "{fieldName}" is replaced with the actual field name.
     *
     */
    private String field;

    /**
     * The error message pertaining to the specific field.
     */
    private String message;

}