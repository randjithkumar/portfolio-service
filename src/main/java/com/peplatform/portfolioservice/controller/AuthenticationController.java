package com.peplatform.portfolioservice.controller;

import com.peplatform.portfolioservice.common.util.ApiResponseBuilder;
import com.peplatform.portfolioservice.dto.request.*;
import com.peplatform.portfolioservice.dto.response.*;
import com.peplatform.portfolioservice.service.api.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;


/**
 * A RESTful controller responsible for user registration and login functionalities.
 * It exposes the necessary endpoints to create new users and authenticate existing ones.
 *
 * @author Randjith V S
 * @version 1.0.0
 * @email mailto:randjithkumar@no-reply.github.com
 * @date 2026.07.11
 * @since 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    /**
     * Authentication service
     */
    private final AuthenticationService authenticationService;

    /**
     * Registers a new user by provided details.
     *
     * <p>
     * This method performs the following steps:
     * - Validates the provided registration request.
     * - Retrieves existing user data if a username already exists.
     * - Creates a new user entry in the system using the validated data.
     * - Generates and returns a new JWT (JSON Web Token) for successful authentication.
     *
     * @param request The login request containing username, email, password, etc.
     * @return A ResponseEntity indicating the success of the operation along with an API response containing the token.
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(
            @Valid @RequestBody RegisterUserRequest request
    ) {
        authenticationService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponseBuilder.success(
                                HttpStatus.CREATED,
                                "User registered successfully.",
                                null
                        )
                );
    }

    /**
     * Authenticates a user.
     * <p>
     * Retrieves an existing user's information, validates it, and returns a JWT token for user authentication.
     *
     * @param request The login request containing username and password.
     * @return A ResponseEntity containing the API response with:
     * - status: HTTP.Status.OK if the login is successful.
     * - body: An ApiResponse object containing the token along with success message.
     *
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> login(
            @Valid @RequestBody LoginRequest request
    ) {
        JwtResponse response =
                authenticationService.login(request);

        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        HttpStatus.OK,
                        "Authentication successful.",
                        response
                )
        );
    }
}