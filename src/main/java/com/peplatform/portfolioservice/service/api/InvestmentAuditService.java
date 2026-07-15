package com.peplatform.portfolioservice.service.api;

import com.peplatform.portfolioservice.kafka.event.InvestmentCreatedEvent;
import com.peplatform.portfolioservice.kafka.model.KafkaMessageMetadata;

public interface InvestmentAuditService {

    void processInvestmentCreatedEvent(
            InvestmentCreatedEvent event,
            KafkaMessageMetadata metadata
    );
}