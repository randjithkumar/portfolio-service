package com.peplatform.portfolioservice.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peplatform.portfolioservice.dto.response.ErrorResponse;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;


/**
 * A REST access denied handler class responsible for responding to HTTP requests that provide insufficient privileges.
 * It uses the ObjectMapper for data mapping and constructs an ErrorResponse to return with the appropriate HTTP status, error code, message, path, timestamp, and field errors.
 *
 * @author Randjith
 * @version 1.0.0
 * @email randjithkumar@no-reply.github.com
 * @date 2026.07.11
 * @since 0.0.1-SNAPSHOT
 */
@Component
@RequiredArgsConstructor
public class RestAccessDeniedHandler
        implements AccessDeniedHandler {

    /**
     * Jackson object mapper
     */
    private final ObjectMapper objectMapper;


    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException exception
    ) throws IOException {

        ErrorResponse error = ErrorResponse.builder()
                .success(false)
                .status(HttpStatus.FORBIDDEN.value())
                .errorCode("ACCESS_DENIED")
                .message("You do not have permission to access this resource.")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .fieldErrors(List.of())
                .build();

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        objectMapper.writeValue(
                response.getOutputStream(),
                error
        );
    }
}