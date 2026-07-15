package com.peplatform.portfolioservice.service.api;

import java.util.UUID;

public interface EventIdempotencyService {

    boolean alreadyProcessed(UUID eventId);

    void markProcessed(
            UUID eventId,
            String eventType,
            String consumer);
}