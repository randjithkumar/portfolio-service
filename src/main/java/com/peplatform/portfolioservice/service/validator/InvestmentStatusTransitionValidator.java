package com.peplatform.portfolioservice.service.validator;

import com.peplatform.portfolioservice.common.enums.InvestmentStatus;
import com.peplatform.portfolioservice.exception.BusinessRuleException;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
 * Utility class for validating investment status transitions.
 *
 * <p>This utility object provides a method to validate the validity of an investment status transition,
 * ensuring that the transition adheres to predetermined rules defined in the system.
 *
 * @author Randjith
 * @version 1.0.0
 * @email "mailto:randjithkumar@no-reply@github.com"
 * @date 2026.07.11
 * @since 1.0.0
 */
@Component
public class InvestmentStatusTransitionValidator {

    /**
     * Mapping of investment statuses to their allowed transitions.
     */
    private static final Map<InvestmentStatus, Set<InvestmentStatus>>
            ALLOWED_TRANSITIONS = new EnumMap<>(InvestmentStatus.class);

    static {
        ALLOWED_TRANSITIONS.put(
                InvestmentStatus.PENDING,
                EnumSet.of(
                        InvestmentStatus.VALIDATED,
                        InvestmentStatus.FAILED,
                        InvestmentStatus.CANCELLED
                )
        );

        ALLOWED_TRANSITIONS.put(
                InvestmentStatus.VALIDATED,
                EnumSet.of(
                        InvestmentStatus.CONFIRMED,
                        InvestmentStatus.FAILED,
                        InvestmentStatus.CANCELLED
                )
        );

        ALLOWED_TRANSITIONS.put(
                InvestmentStatus.CONFIRMED,
                EnumSet.of(InvestmentStatus.REVERSED)
        );

        ALLOWED_TRANSITIONS.put(
                InvestmentStatus.FAILED,
                EnumSet.noneOf(InvestmentStatus.class)
        );

        ALLOWED_TRANSITIONS.put(
                InvestmentStatus.CANCELLED,
                EnumSet.noneOf(InvestmentStatus.class)
        );

        ALLOWED_TRANSITIONS.put(
                InvestmentStatus.REVERSED,
                EnumSet.noneOf(InvestmentStatus.class)
        );
    }

    public void validate(
            InvestmentStatus currentStatus,
            InvestmentStatus requestedStatus
    ) {
        if (currentStatus == requestedStatus) {
            throw new BusinessRuleException(
                    "Investment is already in status: " + currentStatus
            );
        }

        Set<InvestmentStatus> allowedStatuses =
                ALLOWED_TRANSITIONS.getOrDefault(
                        currentStatus,
                        Set.of()
                );

        if (!allowedStatuses.contains(requestedStatus)) {
            throw new BusinessRuleException(
                    "Invalid investment status transition from "
                            + currentStatus
                            + " to "
                            + requestedStatus
            );
        }
    }
}