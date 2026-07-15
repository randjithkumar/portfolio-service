package com.peplatform.portfolioservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a user login request.
 * <p>
 * This entity represents the data structure expected by the user login service. It contains fields such as
 * {@link #username} and {@link #password}, which are required for authenticating a user.
 *
 * @author Randjith
 * @version 1.0.0
 * @email "mailto:randjithkumar@no-reply@github.com"
 * @date 2026.07.11
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
public class LoginRequest {

    /**
     * User identifier for authentication.
     */
    @NotBlank
    private String username;

    /**
     * **Password** - This field represents the user's password. The value of this field must not be blank as indicated by the `@NotBlank` annotation.
     */
    @NotBlank
    private String password;

}