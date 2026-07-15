package com.peplatform.portfolioservice.repository;

import com.peplatform.portfolioservice.entity.Investment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

/**
 * Investment repository class for investing related data, including querying and retrieving information.
 *
 * @author Randjith
 * @version 1.0.0
 * @email "mailto:randjithkumar@no-reply@github.com"
 * @date 2026.07.11
 * @since 1.0.0
 */
public interface InvestmentRepository extends JpaRepository<Investment, Long> {

    /**
     * Retrieves an investment by the specified identifier.
     *
     * <p>
     * Finds the investment record with the specified unique identification (idempotencyKey).
     * {@see InvestmentEntity#getIdentityKey()}
     * </p>
     *
     * @param idempotencyKey The uniquely identifying key of the investment
     * @return An optional containing the investment resource, or empty if not found
     */
    @EntityGraph(attributePaths = {"investor", "fund"})
    Optional<Investment> findByIdempotencyKey(String idempotencyKey);

    /**
     * Retrieves all investments for a given investor ID.
     *
     * @param investorId The ID of the investor.
     * @return A list of {@link Investment} objects for the specified investor.
     */
    @EntityGraph(attributePaths = {"investor", "fund"})
    List<Investment> findByInvestorId(Long investorId);

    /**
     * Retrieves all investments for a given fund ID.
     *
     * @param fundId The ID of the fund.
     * @return A list of {@link Investment} objects for the specified fund.
     */
    @EntityGraph(attributePaths = {"investor", "fund"})
    List<Investment> findByFundId(Long fundId);

    /**
     * Determines whether this repository contains an investment record with the specified investor ID.
     *
     * @param investorId The ID of the investor to check for.
     * @return {@code true} if there is at least one investment for the given investor, otherwise `false`.
     */
    boolean existsByInvestorId(Long investorId);

    /**
     * Determines whether this repository contains an investment record with the specified `fund ID`.
     *
     * @param fundId The ID of the fund to check for.
     * @return {@code true} if there is at least one investment for the given `fund`, otherwise `false`.
     */
    boolean existsByFundId(Long fundId);
}