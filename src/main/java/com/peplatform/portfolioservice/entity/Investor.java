package com.peplatform.portfolioservice.entity;

import com.peplatform.portfolioservice.common.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Investor service class
 * <p>Provides business logic processing related to investors, including operations such as querying, creating, updating, and deleting investors
 *
 * @author Randjith
 * @version 1.0.0
 * @email "mailto:randjithkumar@no-reply@github.com"
 * @date 2026.07.10
 * @since 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "investors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Investor extends BaseEntity {

    /**
     * Unique identifier for the investor.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Unique code assigned to the investor.
     */
    @Column(name = "investor_code", nullable = false, unique = true, length = 50)
    private String investorCode;

    /**
     * Full name of the investor.
     */
    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    /**
     * Email address of the investor.
     */
    @Column(nullable = false, length = 255)
    private String email;

    /**
     * Country of residence of the investor.
     */
    @Column(nullable = false, length = 100)
    private String country;

    /**
     * Risk profile classification of the investor.
     */
    @Column(name = "risk_profile", nullable = false, length = 50)
    private String riskProfile;

    /**
     * Total amount committed by the investor.
     */
    @Column(name = "committed_amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal committedAmount;

//    /**
//     * Timestamp when the investor record was created.
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