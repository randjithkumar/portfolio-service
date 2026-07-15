package com.peplatform.portfolioservice.repository;

import com.peplatform.portfolioservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


/**
 * Repository providing CRUD operations for a user entity. This component interacts with the database and handles data retrieval, insertion, updating, and deletion.
 *
 * @author Randjith
 * @version 1.0.0
 * @email "mailto:randjithkumar@no-reply@github.com"
 * @date 2026.07.11
 * @since 0.0.1-SNAPSHOT
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Retrieves the user's name by user ID.
     *
     * <p>Finds the user by the specified user ID and returns the user's name.</p>
     *
     * @param id The user ID
     * @return The user's name, or {@code null} if not found
     */
    Optional<User> findByUsername(Long id);

    /**
     * Determines if a user with the given username exists.
     *
     * @param username The username to check for.
     * @return true if a user with the specified username exists, false otherwise.
     */
    boolean existsByUsername(String username);

    /**
     * Checks if a user with the specified email already exists.
     *
     * @param email The email of the user to check
     * @return true if a user with the specified email exists, false otherwise
     */
    boolean existsByEmail(String email);

}
