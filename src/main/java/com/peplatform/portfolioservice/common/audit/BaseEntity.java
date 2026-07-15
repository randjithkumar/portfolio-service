package com.peplatform.portfolioservice.common.audit;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Base entity class that provides common fields and logic for entities.
 *
 * <p>This base entity class includes methods for setting the creation and modification times and providing an identifier that is unique to each entity instance.</p>
 *
 * @author Randjith
 * @version 1.0.0
 * @email "mailto:randjithkumar@no-reply@github.com"
 * @date 2025.10.24
 * @since 1.0.0
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    /**
     * Date and time when the object was initially created.
     * <p>Important for auditing purposes to track changes in entity state.</p>
     *
     * @see AuditorAware
     */
    @CreatedDate
    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    /**
     * Last modification timestamp for the entity.
     *
     * <p>The {@code @LastModifiedDate} annotation is used to indicate that this field stores the last time the entity was modified,
     * typically updated through a database trigger after an UPDATE operation. This helps in tracking changes over time, which can be crucial for auditing, backups, and replication purposes.
     *
     * @see LocalDateTime
     */
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * User creation time.
     * <p>Record the time when a user is created in the system. The value of this field remains constant after initialization and changes at each update.</p>
     *
     * @see LocalDateTime
     */
    @CreatedBy
    @Column(
            name = "created_by",
            length = 100,
            updatable = false
    )
    private String createdBy;

    /**
     * Accessor for 'Updated By' field in the `BaseEntity` class.
     */
    @LastModifiedBy
    @Column(name = "updated_by",
            length = 100)
    private String updatedBy;

    /**
     * Version tracking value for optimistic locking.
     */
    @Version
    @Column(nullable = false)
    private Long version;
}