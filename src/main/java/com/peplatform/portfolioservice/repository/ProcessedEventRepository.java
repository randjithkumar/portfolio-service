package com.peplatform.portfolioservice.repository;

import com.peplatform.portfolioservice.entity.ProcessedEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProcessedEventRepository
        extends JpaRepository<ProcessedEvent, Long> {

    boolean existsByEventIdAndConsumerName(
            UUID eventId,
            String consumerName
    );

    Optional<ProcessedEvent> findByEventIdAndConsumerName(
            UUID eventId,
            String consumerName
    );
}