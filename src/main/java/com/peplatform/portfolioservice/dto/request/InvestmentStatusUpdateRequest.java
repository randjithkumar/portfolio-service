package com.peplatform.portfolioservice.dto.request;

import com.peplatform.portfolioservice.common.enums.InvestmentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * Service layer for handling investment status updates.
 * <p>This service interacts with the database and provides interfaces to add, retrieve, update,
 * and delete investment status data. It encapsulates business rules and logic specific to investor
 * transactions.
 *
 * @author Randjith
 * @version 1.0.0
 * @email "mailto:randjithkumar@no-reply@github.com"
 * @date 2026.07.11
 * @since 1.0.0
 */
@Getter
@Setter
public class InvestmentStatusUpdateRequest {

    /**
     * A field representing the status of an investment update request.
     */
    @NotNull
    private InvestmentStatus status;
}