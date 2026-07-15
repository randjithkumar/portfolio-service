package com.peplatform.portfolioservice.entity;

import com.peplatform.portfolioservice.common.audit.BaseEntity;
import com.peplatform.portfolioservice.common.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;


/**
 * {@link ApplicationUser} is the domain entity responsible for managing user data in the system.
 * It extends from {@code BaseEntity} and provides methods for CRUD operations on user information such as
 * retrieval, creation, updating, and deletion. The entity includes fields for username, password, email,
 * role, account enabled status, and account non-locked status. The use of an @Entity annotation in
 * Java indicates that this class is a persistent entity, which means it will interact with a database or other storage system.
 * The table specified using the @Table annotation corresponds to the `application_users` table in the database,
 * where data related to users are stored. Unique constraints are defined for usernames and emails to ensure
 * uniqueness across all entries in the database.
 * {
 * "username": "portfolio.admin",
 * "password": "AdminPassword@2026"
 * }
 *
 * @author Randjith
 * @version 1.0.0
 * @email randjithkumar@no-reply.github.com
 * @date 2026.07.11
 * @since 0.0.1-SNAPSHOT
 */
@Entity
@Table(
        name = "application_users",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_application_users_username",
                        columnNames = "username"
                ),
                @UniqueConstraint(
                        name = "uk_application_users_email",
                        columnNames = "email"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationUser extends BaseEntity {

    /**
     * Primary key of the ApplicationUser entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Username associated with the user account.
     * <p>The username is a unique identifier used to authenticate users and cannot be changed once set.
     *
     * @see ApplicationUser#setUsername(String)
     * @see ApplicationUser# getUsername()
     * <a href="https://docs.example.com/user-username">User Username Details</a>
     */
    @Column(nullable = false, length = 100)
    private String username;

    /**
     * Password for user authentication.
     * <p>Mentioned within the Entity definition and includes a constraint against null and minimum length requirements.</p>
     *
     * @NotNull
     * @Column(length=255)
     */
    @Column(nullable = false, length = 255)
    private String password;

    /**
     * The user's email address.
     * <p>Unique among all users in the system, and not permitted to be null.</p>
     * <a href="classpath:example/email_example.txt">Email Address Example</a>
     */
    @Column(nullable = false, length = 255)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private UserRole role;

    /****
     * Enables the user account.
     *
     * @see #isAccountNonLocked()
     * @see BaseEntity-enabled Property Default value is true.
     */
    @Builder.Default
    @Column(nullable = false)
    private boolean enabled = true;

    /**
     * Indicates whether the entity is non-locked in terms of account security.
     */
    @Builder.Default
    @Column(name = "account_non_locked", nullable = false)
    private boolean accountNonLocked = true;
}