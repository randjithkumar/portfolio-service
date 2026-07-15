package com.peplatform.portfolioservice.service.validator;

import com.peplatform.portfolioservice.common.enums.FundStatus;
import com.peplatform.portfolioservice.common.enums.InvestmentStatus;
import com.peplatform.portfolioservice.entity.Fund;
import com.peplatform.portfolioservice.entity.Investment;
import com.peplatform.portfolioservice.entity.Investor;
import com.peplatform.portfolioservice.exception.BusinessRuleException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * Investment service class
 * <p>
 * Provides business logic processing related to investments, including operations such as querying, creating, updating, and deleting investments.
 *
 * @author Randjith
 * @version 1.0.0
 * @email "mailto:randjithkumar@no-reply@github.com"
 * @date 2026.07.11
 * @since 1.0.0
 */
@Component
public class InvestmentBusinessValidator {

    /**
     * Set of investment statuses that require attention to consume committed funds.
     *
     * @see InvestmentStatus
     */
    private static final Set<InvestmentStatus> COMMITMENT_CONSUMING_STATUSES =
            EnumSet.of(
                    InvestmentStatus.PENDING,
                    InvestmentStatus.VALIDATED,
                    InvestmentStatus.CONFIRMED
            );

    /**
     * Validates a new investment based on various rules.
     *
     * <p>This method checks the following conditions:
     * - The invested amount is greater than zero.
     * - The fund status must be active.
     * - The fund's NAV (Net Asset Value) must be greater than zero.
     * - The remaining commitment from the investor's committed amounts combined with their existing investments cannot exceed the requested investment amount.</p>
     *
     * @param investor            The investor who is creating the new investment.
     * @param fund                The fund to which the investment is being made.
     * @param requestedAmount     The amount of money requested for the new investment.
     * @param existingInvestments A list of existing investments.
     */
    public void validateNewInvestment(
            Investor investor,
            Fund fund,
            BigDecimal requestedAmount,
            List<Investment> existingInvestments
    ) {
        validateAmount(requestedAmount);
        validateFundStatus(fund);
        validateFundNav(fund);
        validateRemainingCommitment(
                investor,
                requestedAmount,
                existingInvestments
        );
    }

    /**
     * Validates a new investment based on various rules.
     *
     * <p>This method checks the following conditions:
     * - The invested amount is greater than zero.
     * - The fund status must be active.
     * - The fund's NAV(Net Asset Value)must be greater than zero.
     * - The remaining commitment from the investor's committed amounts combined with their existing investments cannot exceed the requested investment amount.</p>
     *
     * @param investor            The investor who is creating the new investment.
     * @param fund                The fund to which the investment is being made.
     * @param requestedAmount     The amount of money requested for the new investment.
     * @param existingInvestments A list of existing investments.
     */
    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessRuleException(
                    "Investment amount must be greater than zero"
            );
        }
    }

    /**
     * Retrives the user's name by user ID.
     */
    private void validateFundStatus(Fund fund) {
        if (fund.getStatus() != FundStatus.ACTIVE) {
            throw new BusinessRuleException(
                    "Investment cannot be created because fund "
                            + fund.getFundCode()
                            + " is not ACTIVE"
            );
        }
    }

    private void validateFundNav(Fund fund) {
        if (fund.getNav() == null
                || fund.getNav().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessRuleException(
                    "Investment cannot be created because fund NAV "
                            + "must be greater than zero"
            );
        }
    }

    /**
     * Validates the remaining investment amount by comparing it to the committed and consumed amounts.
     *
     * @param investor            The user who is investing.
     * @param requestedAmount     The amount of money being requested for the investment.
     * @param existingInvestments A list of investments that have already been processed.
     */
    private void validateRemainingCommitment(
            Investor investor,
            BigDecimal requestedAmount,
            List<Investment> existingInvestments
    ) {
        BigDecimal committedAmount =
                defaultZero(investor.getCommittedAmount());

        BigDecimal consumedCommitment = existingInvestments.stream()
                .filter(investment ->
                        COMMITMENT_CONSUMING_STATUSES.contains(
                                investment.getStatus()
                        )
                )
                .map(Investment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal remainingCommitment =
                committedAmount.subtract(consumedCommitment);

        if (requestedAmount.compareTo(remainingCommitment) > 0) {
            throw new BusinessRuleException(
                    "Investment amount "
                            + requestedAmount
                            + " exceeds remaining commitment "
                            + remainingCommitment
            );
        }
    }

    private BigDecimal defaultZero(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}