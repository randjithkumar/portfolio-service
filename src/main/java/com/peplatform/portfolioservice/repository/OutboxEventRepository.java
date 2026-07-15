package com.peplatform.portfolioservice.repository;

import com.peplatform.portfolioservice.common.enums.OutboxStatus;
import com.peplatform.portfolioservice.entity.OutboxEvent;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * **Repository Interface for Outbox Events**
 * <p>
 * This interface acts as a specification for querying the outbox event repository. It extends the JpaRepository provided by Spring Data JPA and is designed to be used directly by developers without needing further configuration.
 * <p>
 * The primary responsibilities of this repository are:
 * 1. **Existence Check**: The `existsByEventId` method checks if an outbox event with a specific ID exists in the database. This method is useful for ensuring data integrity before executing write operations on the event.
 * 2. **Locking Mechanism**: The `findPublishableEventsForUpdate` method performs optimistic locking on the OutboxEvent entity based on its status and retry timestamp. This ensures that only a single transaction can update an outbox event at a time, avoiding race conditions and inconsistencies.
 * 3. **Query for Publishable Events**: The method `findPublishableEventsForUpdate` queriesOutbox events from the database that are either pending or have failed with a retry window that has not expired. It orders the results in ascending order of the creation timestamp to ensure that the first event (the one with the smallest createdAt) is updated first.
 * 4. **Query Parameters**: The method accepts parameters for filtering, including `pendingStatus`, `failedStatus`, and the current date and time. These parameters are used in the query conditions to limit the scope of the events returned by the method.
 * <p>
 * To use this repository, you can inject it into your services or methods as required. It provides a straightforward way to interact with the outbox event data through the defined operations supported by the JPA abstraction.
 *
 * @author Randjith
 * @version 1.0.0
 * @email randjithkumar@no-reply.github.com
 * @date 2026.07.13
 * @since 0.0.1-SNAPSHOT
 */
public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {

    /**
     * Checks if an OutboxEvent with the specified event ID exists in the repository.
     *
     * @param eventId The unique identifier of the event
     * @return true if an OutboxEvent exists with the given ID, false otherwise
     */
    boolean existsByEventId(UUID eventId);


    /**
     * Repository interface for Outbox Events.
     *
     * <p>
     * This interface acts as a specification for querying the outbox event repository. It extends the JpaRepository provided by Spring Data JPA and is designed to be used directly by developers without needing further configuration.
     * </p>
     * <p>
     * The primary responsibilities of this repository are:
     * 1. **Existence Check**: The `existsByEventId` method checks if an outbox event with a specific ID exists in the database. This method is useful for ensuring data integrity before executing write operations on the event.
     * 2. **Locking Mechanism**: The `findPublishableEventsForUpdate` method performs optimistic locking on the OutboxEvent entity based on its status and retry timestamp. This ensures that only a single transaction can update an outbox event at a time, avoiding race conditions and inconsistencies.
     * 3. **Query for Publishable Events**: The method `findPublishableEventsForUpdate` queriesOutbox events from the database that are either pending or have failed with a retry window that has not expired. It orders the results in ascending order of the creation timestamp to ensure that the first event(the one with the smallest createdAt)is updated first.
     * 4. **Query Parameters**: The method accepts parameters for filtering, including `pendingStatus`,`failedStatus`,and the current date and time. These parameters are used in the query conditions to limit the scope of the events returned by the method.
     * </p>
     * <p>
     * To use this repository,you can inject it into your services or methods as required. It provides a straightforward way to interact with the outbox event data through the defined operations supported by the JPA abstraction.
     * </p>
     *
     * @author Randjith
     * @version 1.0.0
     * @email randjithkumar@no-reply.github.com
     * @date 2026.07.13
     * @since 0.0.1-SNAPSHOT
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select event
            from OutboxEvent event
            where (
                event.status = :pendingStatus
                or (
                    event.status = :failedStatus
                    and event.nextRetryAt <= :now
                )
            )
            order by event.createdAt asc
            """)
    List<OutboxEvent> findPublishableEventsForUpdate(
            @Param("pendingStatus") OutboxStatus pendingStatus,
            @Param("failedStatus") OutboxStatus failedStatus,
            @Param("now") LocalDateTime now,
            Pageable pageable
    );


    /**
     * Resets stale processing events that have reached the specified cutoff.
     *
     * <p>
     * This method updates all OutboxEvents whose status is 'processing' to 'pending'
     * if they are older than the provided cutoff date. The cutoff is defined as the date and time at which
     * no further updates should be made to these event records. This helps in managing stale or abandoned processing tasks,
     * ensuring that newer events have access to resources while oldstasked ones undergo cleanup.
     *
     * @param processingStatus The target status for the update, typically 'PENDING'.
     * @param pendingStatus    The new target status for the update, typically 'PROCESSING'.
     * @param cutoff           The date and time after which no further updates should be made to these event records.
     * @return The number of stale processing events that were reset.
     */
    @Modifying
    @Query("""
            update OutboxEvent event
            set event.status = :pendingStatus
            where event.status = :processingStatus
              and event.updatedAt < :cutoff
            """)
    int resetStaleProcessingEvents(
            @Param("processingStatus") OutboxStatus processingStatus,
            @Param("pendingStatus") OutboxStatus pendingStatus,
            @Param("cutoff") LocalDateTime cutoff
    );

}