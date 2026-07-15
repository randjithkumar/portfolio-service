package com.peplatform.portfolioservice.repository;

import com.peplatform.portfolioservice.entity.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * User repository interface
 * <p>Provides access to user data,
 * supporting queries and operations for users such as finding by username or email.</p>
 *
 * @author Randjith
 * @version 1.0.0
 * @email "mailto:randjithkumar@no-reply@github.com"
 * @since 0.0.1-SNAPSHOT
 */
public interface ApplicationUserRepository
        extends JpaRepository<ApplicationUser, Long> {

    /**
     * Retrieves the user by their username.
     *
     * @param username The username of the user to search for
     * @return An Optional containing the ApplicationUser object of the found user, or an empty Optional if not found
     */
    Optional<ApplicationUser> findByUsernameIgnoreCase(String username);

    /**
     * Determines if a user with the given username exists in the repository.
     * <p>
     * Retrieves an `Optional` that contains the `User` object if a user with the provided username exists, or an empty {@code Optional} otherwise.
     *
     * @param username The username to search for
     * @return An {@link Optional} containing the `User` object if it exists, or an empty {@code Optional} otherwise
     */
    boolean existsByUsernameIgnoreCase(String username);

    /**
     * Checks if an user exists by the specified email.
     * <p>Looks up users in the repository based on their email address and returns a boolean indicating whether a user with that email exists.</p>
     *
     * @param email The User'sEmail
     * @return true If such a user exists, false otherwise
     */
    boolean existsByEmailIgnoreCase(String email);
}