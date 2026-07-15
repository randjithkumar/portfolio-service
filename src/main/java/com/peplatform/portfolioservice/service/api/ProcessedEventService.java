package com.peplatform.portfolioservice.service.api;

import com.peplatform.portfolioservice.common.enums.ProcessedEventStatus;
import com.peplatform.portfolioservice.entity.ProcessedEvent;
import com.peplatform.portfolioservice.kafka.event.PortfolioEvent;
import com.peplatform.portfolioservice.kafka.model.KafkaMessageMetadata;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface ProcessedEventService {

    boolean reserveEvent(
            PortfolioEvent event,
            String consumerName,
            KafkaMessageMetadata metadata
    );

    void markCompleted(
            java.util.UUID eventId,
            String consumerName
    );

    void markFailed(
            java.util.UUID eventId,
            String consumerName,
            String failureReason
    );


}