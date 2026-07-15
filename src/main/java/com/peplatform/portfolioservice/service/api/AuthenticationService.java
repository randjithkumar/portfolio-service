package com.peplatform.portfolioservice.service.api;

import com.peplatform.portfolioservice.dto.request.LoginRequest;
import com.peplatform.portfolioservice.dto.request.RegisterUserRequest;
import com.peplatform.portfolioservice.dto.response.JwtResponse;


/**
 * **AuthenticationService** provides methods to handle user authentication and registration.
 * <p>It exposes the following functionalities:
 * - Login functionality to authenticate users with provided credentials.
 * - Registration functionality to create new users in the system.
 *
 * @author Randjith
 * @version 1.0.0
 * @email randjithkumar@no-reply.github.com
 * @date 2026.07.11
 * @since 0.0.1-SNAPSHOT
 */
public interface AuthenticationService {

    /**
     * Retrieves a user's JWT response by login credentials.
     *
     * @param request The login request containing the user credentials
     * @return A JWTResponse object containing the user's token and role information
     */
    JwtResponse login(LoginRequest request);

    /**
     * Registers a new user.
     *
     * <p>Inputs:</p>
     * <ul>
     *
     * <li><code>request</code>: The registration request.</li>
     *
     * </ul>
     *
     * <p>Returns:</p>
     * <ul>
     *
     * <li>A <code>UserResponse</code> containing the registered user's information.</li>
     *
     * </ul>
     *
     * @param request The registration request object.
     */
    void register(RegisterUserRequest request);
}