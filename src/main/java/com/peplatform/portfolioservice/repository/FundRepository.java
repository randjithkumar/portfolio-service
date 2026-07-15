package com.peplatform.portfolioservice.repository;

import com.peplatform.portfolioservice.entity.Fund;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface for fund repository providing data access operations for Fund entities.
 * <p>Extends {@link JpaRepository} to inherit standard CRUD operations and defines a custom method {@code existsByFundCode} to check
 * the existence of a fund by its unique fund code.
 *
 * @author Randjith
 * @version 1.0.0
 * @email "mailto:randjithkumar@no-reply@github.com"
 * @date 2026.07.10
 * @since 0.0.1-SNAPSHOT
 */
public interface FundRepository extends JpaRepository<Fund, Long> {

    /**
     * Checks if a fund exists with the given fund code.
     *
     * @param fundCode the fund code to check
     * @return true if a fund exists with the given code, false otherwise
     */
    boolean existsByFundCode(String fundCode);

    /**
     * Retrieves a fund by its unique fund code that does not match the specified ID.
     *
     * <p>
     * This method checks if a fund with a specific fund code (identified by `fundCode`) exists in the database and ensures it is not associated with the given ID (`id`). If such a fund exists, the method returns true; otherwise, it returns false.
     * </p>
     *
     * @param fundCode The unique fund code to search for
     * @param id       The ID of a fund to exclude from the result
     * @return true if a fund with `fundCode` and not equal to `id` exists in the database, false otherwise
     */
    boolean existsByFundCodeAndIdNot(
            String fundCode,
            Long id
    );
}