package com.peplatform.portfolioservice.entity;

import com.peplatform.portfolioservice.common.audit.BaseEntity;
import com.peplatform.portfolioservice.common.enums.InvestmentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "investment_audit_events",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_investment_audit_event_id",
                        columnNames = "event_id"
                )
        },
        indexes = {
                @Index(
                        name = "idx_investment_audit_investment",
                        columnList = "investment_id"
                ),
                @Index(
                        name = "idx_investment_audit_investor",
                        columnList = "investor_id"
                ),
                @Index(
                        name = "idx_investment_audit_fund",
                        columnList = "fund_id"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvestmentAuditEvent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "event_id",
            nullable = false,
            unique = true
    )
    private UUID eventId;

    @Column(
            name = "event_type",
            nullable = false,
            length = 100
    )
    private String eventType;

    @Column(
            name = "event_version",
            nullable = false,
            length = 20
    )
    private String eventVersion;

    @Column(
            name = "correlation_id",
            nullable = false,
            length = 100
    )
    private String correlationId;

    @Column(
            name = "occurred_at",
            nullable = false
    )
    private Instant occurredAt;

    @Column(
            name = "investment_id",
            nullable = false
    )
    private Long investmentId;

    @Column(
            name = "investor_id",
            nullable = false
    )
    private Long investorId;

    @Column(
            name = "investor_code",
            nullable = false,
            length = 50
    )
    private String investorCode;

    @Column(
            name = "fund_id",
            nullable = false
    )
    private Long fundId;

    @Column(
            name = "fund_code",
            nullable = false,
            length = 50
    )
    private String fundCode;

    @Column(
            nullable = false,
            precision = 18,
            scale = 2
    )
    private BigDecimal amount;

    @Column(
            nullable = false,
            precision = 18,
            scale = 4
    )
    private BigDecimal nav;

    @Column(
            nullable = false,
            precision = 18,
            scale = 4
    )
    private BigDecimal units;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 30
    )
    private InvestmentStatus status;
}