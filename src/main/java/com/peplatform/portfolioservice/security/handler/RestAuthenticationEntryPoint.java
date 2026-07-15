package com.peplatform.portfolioservice.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peplatform.portfolioservice.dto.response.ErrorResponse;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;


/**
 * **Rest Authentication Entry Point**: A component responsible for managing authentication entry points in the system. This class provides a RESTful API to authenticate users by handling unauthorized requests and returning appropriate responses.
 *
 * @author Randjith
 * @version 1.0.0
 * @email randjithkumar@no-reply.github.com
 * @date 2026.07.11
 * @since 0.0.1-SNAPSHOT
 */
@Component
@RequiredArgsConstructor
public class RestAuthenticationEntryPoint
        implements AuthenticationEntryPoint {

    /**
     * Describes the mapping and handling of JSON input in the `RestAuthenticationEntryPoint` class.
     *
     * @see ObjectMapper
     **/
    private final ObjectMapper objectMapper;

    /**
     * Intercepts an attempt to authenticate a user and provides a fallback
     * response. Upon authentication failure, it constructs a failed
     * response detailing the authentication requirement.
     *
     * @param request   The HTTP request
     * @param response  The HTTP response
     * @param exception The authentication exception that occurred
     * @throws IOException Thrown if there was an I/O error during the processing
     */
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException {

        ErrorResponse error = ErrorResponse.builder()
                .success(false)
                .status(HttpStatus.UNAUTHORIZED.value())
                .errorCode("AUTHENTICATION_REQUIRED")
                .message("Authentication is required.")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .fieldErrors(List.of())
                .build();

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        objectMapper.writeValue(
                response.getOutputStream(),
                error
        );
    }
}