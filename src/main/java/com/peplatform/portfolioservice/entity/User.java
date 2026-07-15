package com.peplatform.portfolioservice.entity;

import com.peplatform.portfolioservice.common.audit.BaseEntity;
import com.peplatform.portfolioservice.common.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

/**
 * Defines the User entity class, which is responsible for storing user data and relationships with other entities in a system.
 * <p>
 * The User entity contains fields for storing:
 * - `id` as a unique identifier for each user.
 * - `username` as a required string that represents the username of the user.
 * - `password` as a non-null string to store the secure hashed password of the user.
 * - `email` as a non-null string that uniquely identifies the user's email address.
 * - `role` as a non-nullable enum value from the UserRole enum that specifies the user's role in the system.
 * - `enabled` as a boolean field to indicate whether the user is active or disabled by default, with a default value of `true`.
 * <p>
 * This entity is annotated with @Entity, indicating it maps to a database table named 'users'. It also uses @Getter and @Setter for getter and setter methods on all fields.
 * The @NoArgsConstructor, @AllArgsConstructor, @Builder, and @Table annotations are used for generating constructors, parameterized constructors, builder patterns, and defining the table structure respectively.
 *
 * @author Randjith
 * @version 1.0.0
 * @email randjithkumar@no-reply.github.com
 * @date 2026.07.11
 * @since 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    /** Primary key for the User entity. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Username
     */
    @Column(nullable = false, unique = true, length = 100)
    private String username;

    /**
     * Password of the user account.
     */
    @Column(nullable = false)
    private String password;

    /**
     * User's electronic mail address
     */
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * Role of the user.
     *
     * @see UserRole#ADMIN UserRole.USER UserRole.NONE UserRole.MODERATOR
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    /**
     * Represents the active status of a user.
     * <p>Active users are those who have been granted access to the system, while inactive users are restricted from interacting with it.</p>
     */
    @Column(nullable = false)
    @Builder.Default
    private boolean enabled = true;

}