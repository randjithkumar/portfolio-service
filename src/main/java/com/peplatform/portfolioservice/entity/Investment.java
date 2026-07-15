package com.peplatform.portfolioservice.entity;

import com.peplatform.portfolioservice.common.audit.BaseEntity;
import com.peplatform.portfolioservice.common.enums.InvestmentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * The `Investment` class represents an investment entity in the system, storing information such as user ID, fund ID, amount, NAV, units, and status.
 *
 * @author Randjith
 * @version 1.0.0
 * @email "mailto:randjithkumar@no-reply@github.com"
 * @date 2026.07.10
 * @since 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "investments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Investment extends BaseEntity {

    /**
     * The ID of the investment.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Client-generated key used to make investment creation idempotent.
     */
    @Column(name = "idempotency_key", nullable = false, unique = true, length = 100)
    private String idempotencyKey;

    /**
     * The investor who owns the investment.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "investor_id", nullable = false)
    private Investor investor;

    /**
     * The fund that owns the investment.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fund_id", nullable = false)
    private Fund fund;

    /**
     * The amount invested in the investment.
     */
    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal amount;

    /**
     * The NAV (Net Asset Value) of the investment.
     */
    @Column(nullable = false, precision = 18, scale = 4)
    private BigDecimal nav;

    /**
     * The number of units in the investment.
     */
    @Column(nullable = false, precision = 18, scale = 4)
    private BigDecimal units;

    /**
     * The status of the investment.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private InvestmentStatus status;

    /**
     * The date when the investment was made.
     */
    @Column(name = "investment_date", nullable = false)
    private LocalDateTime investmentDate;

//    /**
//     * The date when the investment was created.
//     */
//    @Column(name = "created_at")
//    private LocalDateTime createdAt;
//
//    /**
//     * Updates the `createdAt` field to the current time before persisting the entity.
//     */
//    @PrePersist
//    public void prePersist() {
//        this.createdAt = LocalDateTime.now();
//        if (this.investmentDate == null) {
//            this.investmentDate = LocalDateTime.now();
//        }
//    }
}
