package com.peplatform.portfolioservice.repository;

import com.peplatform.portfolioservice.entity.InvestmentAuditEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface InvestmentAuditEventRepository
        extends JpaRepository<InvestmentAuditEvent, Long> {

    boolean existsByEventId(UUID eventId);

    List<InvestmentAuditEvent> findByInvestmentIdOrderByOccurredAtAsc(
            Long investmentId
    );
}