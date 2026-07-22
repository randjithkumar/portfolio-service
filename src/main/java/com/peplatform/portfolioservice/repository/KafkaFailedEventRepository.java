package com.peplatform.portfolioservice.repository;

import com.peplatform.portfolioservice.entity.KafkaFailedEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KafkaFailedEventRepository extends JpaRepository<KafkaFailedEvent, Long> {
}
