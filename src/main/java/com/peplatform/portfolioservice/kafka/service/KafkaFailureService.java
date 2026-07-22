package com.peplatform.portfolioservice.kafka.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peplatform.portfolioservice.entity.KafkaFailedEvent;
import com.peplatform.portfolioservice.kafka.event.PortfolioEvent;
import com.peplatform.portfolioservice.repository.KafkaFailedEventRepository;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class KafkaFailureService {
    private final KafkaFailedEventRepository repository;
    private final ObjectMapper objectMapper;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void persist(PortfolioEvent event, ConsumerRecord<String, ?> record, Exception exception) {
        repository.save(KafkaFailedEvent.builder()
                .eventId(event.eventId())
                .eventType(event.eventType())
                .correlationId(event.correlationId())
                .sourceTopic(record.topic())
                .sourcePartition(record.partition())
                .sourceOffset(record.offset())
                .messageKey(record.key())
                .payload(toJson(event))
                .failureReason(rootMessage(exception))
                .build());
    }
    
    private String toJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            return String.valueOf(value);
        }
    }

    private String rootMessage(Throwable throwable) {
        Throwable current = throwable;
        while (current.getCause() != null) current = current.getCause();
        return current.getMessage() == null ? current.getClass().getSimpleName() : current.getMessage();
    }
}
