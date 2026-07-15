package com.peplatform.portfolioservice.repository;

import com.peplatform.portfolioservice.entity.Investor;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Investor repository interface providing data access operations for {@link Investor}
 * entities. This interface extends Spring Data JPA's {@link JpaRepository} to inherit
 * standard CRUD functionality and defines custom queries for investor-specific attributes.
 * <p>Key features:
 * <ul>
 *   <li>Check existence of an investor by its unique code via {@link #existsByInvestorCode(String)}</li>
 *   <li>Retrieve an investor by its code with optional result handling via {@link #findByInvestorCode(String)}</li>
 * </ul>
 * This interface is used by service layers to perform investor-related queries and validations.
 *
 * @author Randjith
 * @version 1.0.0
 * @email "mailto:randjithkumar@no-reply@github.com"
 * @date 2026.07.10
 * @since 0.0.1-SNAPSHOT
 */
public interface InvestorRepository extends JpaRepository<Investor, Long> {

    /**
     * Checks if an investor exists with the given investor code.
     *
     * @param investorCode the investor code to check
     * @return true if an investor exists with the given code, false otherwise
     */
    boolean existsByInvestorCode(String investorCode);

    /**
     * Finds an investor by their investor code.
     *
     * @param investorCode the investor code to search for
     * @return an Optional containing the investor if found, or empty otherwise
     */
    Optional<Investor> findByInvestorCode(String investorCode);

    /**
     * Checks if an investor exists with the given investor code and a different ID.
     *
     * @param investorCode the investor code to check
     * @param id           the ID of the investor to exclude from the search
     *                     <p>This method implements logic to find an investor by their investor code while excluding the specified ID. It is useful in scenarios where duplicate records need to be identified or handled consistently across the system.
     */
    boolean existsByInvestorCodeAndIdNot(String investorCode, Long id);

    /**
     * Retrieves an investor by their ID.
     *
     * @param id The ID of the investor to search for (required)
     * @return An Optional containing the investor if found, or empty otherwise
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select i from Investor i where i.id = :id")
    Optional<Investor> findByIdForUpdate(@Param("id") Long id);
}