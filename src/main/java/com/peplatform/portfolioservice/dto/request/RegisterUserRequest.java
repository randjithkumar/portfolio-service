package com.peplatform.portfolioservice.dto.request;

import com.peplatform.portfolioservice.common.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a representation of the input or output data for registering a new user.
 *
 * <p>User service classes are primarily responsible for handling communication with the database,
 * querying stored data, and updating the database based on user registration requests.
 *
 * @author Randjith
 * @version 1.0.0
 * @email "mailto:randjithkumar@no-reply.github.com"
 * @date 2026.07.11
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
public class RegisterUserRequest {

    /**
     * Username of the user being registered.
     */
    @NotBlank
    @Size(min = 4, max = 100)
    private String username;

    /**
     * Password used for authentication.
     */
    @NotBlank
    @Size(min = 10, max = 100)
    private String password;

    /**
     * Email address provided during registration.
     *
     */
    @Email
    @Size(max = 255)
    private String email;

    /**
     * Specifies the user's role within the application.
     *
     * @see UserRole
     */
    @NotNull
    private UserRole role;

}