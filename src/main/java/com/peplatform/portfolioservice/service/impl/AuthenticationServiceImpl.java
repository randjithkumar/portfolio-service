package com.peplatform.portfolioservice.service.impl;

import com.peplatform.portfolioservice.dto.request.*;
import com.peplatform.portfolioservice.dto.response.JwtResponse;
import com.peplatform.portfolioservice.entity.ApplicationUser;
import com.peplatform.portfolioservice.exception.DuplicateResourceException;
import com.peplatform.portfolioservice.repository.ApplicationUserRepository;
import com.peplatform.portfolioservice.security.jwt.JwtService;
import com.peplatform.portfolioservice.security.user.CustomUserDetails;
import com.peplatform.portfolioservice.service.api.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationServiceImpl
        implements AuthenticationService {

    /**
     * Repository that provides database access to ApplicationUser objects.
     */
    private final ApplicationUserRepository userRepository;
    /**
     * Hashing utility to encode passwords during user registration.
     */
    private final PasswordEncoder passwordEncoder;
    /**
     * Authentication service manager
     */
    private final AuthenticationManager authenticationManager;
    /**
     * Service for handling authentication.
     */
    private final JwtService jwtService;

    @Override
    @Transactional(readOnly = true)
    public JwtResponse login(LoginRequest request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getUsername(),
                                request.getPassword()
                        )
                );

        CustomUserDetails principal =
                (CustomUserDetails) authentication.getPrincipal();

        String token = jwtService.generateAccessToken(principal);

        return JwtResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .expiresIn(jwtService.getExpirationSeconds())
                .username(principal.username())
                .role(
                        com.peplatform.portfolioservice.common.enums
                                .UserRole.valueOf(principal.role())
                )
                .build();
    }

    /**
     * and the closing marker
     */
    @Override
    public void register(RegisterUserRequest request) {

        if (userRepository.existsByUsernameIgnoreCase(
                request.getUsername()
        )) {
            throw new DuplicateResourceException(
                    "Username already exists: "
                            + request.getUsername()
            );
        }

        if (userRepository.existsByEmailIgnoreCase(
                request.getEmail()
        )) {
            throw new DuplicateResourceException(
                    "Email already exists: "
                            + request.getEmail()
            );
        }

        ApplicationUser user = ApplicationUser.builder()
                .username(request.getUsername().trim())
                .password(
                        passwordEncoder.encode(request.getPassword())
                )
                .email(request.getEmail().trim().toLowerCase())
                .role(request.getRole())
                .enabled(true)
                .accountNonLocked(true)
                .build();

        userRepository.save(user);
    }
}