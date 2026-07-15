package com.peplatform.portfolioservice.security.service;

import com.peplatform.portfolioservice.entity.ApplicationUser;
import com.peplatform.portfolioservice.repository.ApplicationUserRepository;
import com.peplatform.portfolioservice.security.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Customized user details service implementation.
 *
 * <p>This service provides the core functionality of handling user account validation and details retrieval. It supports querying, creating,
 * updating, deleting, and obtaining custom user details such as name, email, etc.</p>
 *
 * @author Randjith
 * @version 1.0.0
 * @email "mailto:randjithkumar@no-reply@github.com"
 * @date 2026.07.11
 * @since 0.0.1-SNAPSHOT
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    /**
     * User repository instance for user details operations.
     *
     * @see ApplicationUserRepository
     */
    private final ApplicationUserRepository applicationUserRepository;

    // Add @Lazy right here to break the startup cycle
    public CustomUserDetailsService(@Lazy ApplicationUserRepository applicationUserRepository) {
        this.applicationUserRepository = applicationUserRepository;
    }

    /**
     * Retrieves the user's name by user ID.
     *
     * @param username The user's name
     * @return The user's details
     * @throws UsernameNotFoundException Thrown if the user does not exist
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        ApplicationUser user = applicationUserRepository
                .findByUsernameIgnoreCase(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found: " + username
                        )
                );

        return CustomUserDetails.from(user);
    }
}