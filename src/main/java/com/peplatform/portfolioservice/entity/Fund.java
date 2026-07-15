package com.peplatform.portfolioservice.entity;

import com.peplatform.portfolioservice.common.audit.BaseEntity;
import com.peplatform.portfolioservice.common.enums.AssetClass;
import com.peplatform.portfolioservice.common.enums.FundStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a financial fund entity mapped to the {@code funds} database table.
 * <p>Encapsulates core details of an investment fund, including its identifier,
 * unique fund code, descriptive name, asset classification, currency, net asset
 * value (NAV), inception date, current status, and creation timestamp. The class
 * leverages JPA annotations for persistence and includes a {@code prePersist}
 * callback to automatically set the creation time when a new record is inserted.
 *
 * <p>Typical usage involves storing and retrieving fund information for portfolio
 * management, performance tracking, and reporting purposes.
 *
 * @author Randjith
 * @version 1.0.0
 * @email "mailto:randjithkumar@no-reply@github.com"
 * @date 2026.07.09
 * @since 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "funds")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Fund extends BaseEntity {

    /**
     * Unique identifier for the fund.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Unique code assigned to the fund.
     */
    @Column(name = "fund_code", nullable = false, unique = true, length = 50)
    private String fundCode;

    /**
     * Name of the fund.
     */
    @Column(name = "fund_name", nullable = false, length = 200)
    private String fundName;

    /**
     * Asset class the fund belongs to.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "asset_class", nullable = false, length = 50)
    private AssetClass assetClass;

    /**
     * Currency code (3 letters) for the fund's transactions.
     */
    @Column(nullable = false, length = 3)
    private String currency;

    /**
     * Net Asset Value (NAV) of the fund.
     */
    @Column(nullable = false, precision = 18, scale = 4)
    private BigDecimal nav;

    /**
     * Date when the fund was established.
     */
    @Column(name = "inception_date", nullable = false)
    private LocalDate inceptionDate;

    /**
     * Current status of the fund (e.g., ACTIVE, INACTIVE).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private FundStatus status;

//    /**
//     * Timestamp when the fund record was created.
//     */
//    @Column(name = "created_at")
//    private LocalDateTime createdAt;
//
//    /**
//     * Lifecycle callback to set the creation timestamp before persisting.
//     */
//    @PrePersist
//    public void prePersist() {
//        this.createdAt = LocalDateTime.now();
//    }
}