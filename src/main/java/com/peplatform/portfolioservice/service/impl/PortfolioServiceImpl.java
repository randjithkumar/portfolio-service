package com.peplatform.portfolioservice.service.impl;

import com.peplatform.portfolioservice.common.enums.InvestmentStatus;
import com.peplatform.portfolioservice.dto.response.FundHoldingResponse;
import com.peplatform.portfolioservice.dto.response.PortfolioSummaryResponse;
import com.peplatform.portfolioservice.entity.Fund;
import com.peplatform.portfolioservice.entity.Investment;
import com.peplatform.portfolioservice.entity.Investor;
import com.peplatform.portfolioservice.repository.InvestmentRepository;
import com.peplatform.portfolioservice.repository.InvestorRepository;
import com.peplatform.portfolioservice.service.api.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A service responsible for handling operations related to portfolios, such as querying
 * and retrieving portfolio summaries.
 *
 * @author Randjith
 * @version 1.0.0
 * @email "mailto:randjithkumar@no-reply@github.com"
 * @date 2026.07.10
 * @since 0.0.1-SNAPSHOT
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PortfolioServiceImpl implements PortfolioService {

    /**
     * Enum of investment statuses for holding records
     */
    private static final Set<InvestmentStatus> ACTIVE_HOLDING_STATUSES =
            EnumSet.of(
                    InvestmentStatus.VALIDATED,
                    InvestmentStatus.CONFIRMED
            );

    /**
     * Scale for monetary values
     */
    private static final int MONEY_SCALE = 2;

    /**
     * Scale for unit values
     */
    private static final int UNIT_SCALE = 4;

    /**
     * Constant indicating the precision for percentage calculations.
     */
    private static final int PERCENTAGE_SCALE = 2;

    /**
     * Investor repository for interacting with investors
     */
    private final InvestorRepository investorRepository;

    /**
     * Repository for accessing investment data
     */
    private final InvestmentRepository investmentRepository;

    /**
     * Retrieves the user's portfolio summary by user ID.
     *
     * @param investorId The user ID of the investor whose portfolio summary is to be retrieved.
     * @return A PortfolioSummaryResponse object containing the investor's portfolio details, or null if no portfolio exists for the given user ID.
     */
    @Override
    public PortfolioSummaryResponse getInvestorPortfolioSummary(Long investorId) {
        Investor investor = investorRepository.findById(investorId)
                .orElseThrow(() -> new RuntimeException("Investor not found for id: " + investorId));
// collects the list of investments
//        List<Investment> investments = investmentRepository.findByInvestorId(investorId);
        List<Investment> investments = investmentRepository.findByInvestorId(investorId).stream()
                .filter(investment -> ACTIVE_HOLDING_STATUSES.contains(investment.getStatus()))
                .toList();
// grouping investments based on fundId
        Map<Long, List<Investment>> investmentsByFund = investments.stream()
                .collect(Collectors.groupingBy(investment -> investment.getFund().getId()));
// createFundHolding using list of Investments
        List<FundHoldingResponse> holdings = investmentsByFund.values().stream()
                .map(this::createFundHolding)
                .sorted((first, second) ->
                        first.getFundCode()
                                .compareToIgnoreCase(second.getFundCode())
                )
                .toList();
// summation of investedAmount from FundHoldingResponses list for the totalInvestedAmount
        BigDecimal totalInvestedAmount = holdings.stream()
                .map(FundHoldingResponse::getInvestedAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
// summation of currentValue from FundHoldingResponses list for the currentPortfolioValue
        BigDecimal currentPortfolioValue = holdings.stream()
                .map(FundHoldingResponse::getCurrentValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

// Difference of totalInvestedAmount and currentPortfolioValue
        BigDecimal totalUnrealizedGainLoss = currentPortfolioValue.subtract(totalInvestedAmount);

// Validated for committedAmount.
        BigDecimal committedAmount = defaultZero(investor.getCommittedAmount());

// Difference of totalInvestedAmount and committedAmount
        BigDecimal remainingCommitment = committedAmount.subtract(totalInvestedAmount);

// OverallReturn (%) = (totalUnrealizedGainLoss x 100 / totalInvestedAmount)
        BigDecimal overallReturnPercentage = calculatePercentage(totalUnrealizedGainLoss, totalInvestedAmount);

        return PortfolioSummaryResponse.builder()
                .investorId(investor.getId())
                .investorCode(investor.getInvestorCode())
                .investorName(investor.getFullName())
                .riskProfile(investor.getRiskProfile())
                .committedAmount(scaleMoney(committedAmount))
                .totalInvestmentAmount(scaleMoney(totalInvestedAmount))
                .remainingCommitment(scaleMoney(remainingCommitment))
                .currentPortfolioValue(scaleMoney(currentPortfolioValue))
                .totalUnrealizedGainLoss(
                        scaleMoney(totalUnrealizedGainLoss)
                )
                .overallReturnPercentage(overallReturnPercentage)
                .totalFunds(holdings.size())
                .holdings(holdings)
                .build();
    }

    /**
     * Creates a holding response for a given list of investment entries.
     *
     * <p>Calculates the average purchase NAV, current NAV, and unrealized gain loss based on the provided investment entries.</p>
     *
     * @param fundInvestments List of investment entries to create holding responses from.
     * @return FundHoldingResponse object representing the holding information.
     */
    private FundHoldingResponse createFundHolding(
            List<Investment> fundInvestments
    ) {
        Investment firstInvestment = fundInvestments.getFirst();
        Fund fund = firstInvestment.getFund();

        // total sum of invested amount.
        BigDecimal investedAmount = fundInvestments.stream()
                .map(Investment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // total sum of units.
        BigDecimal totalUnits = fundInvestments.stream()
                .map(Investment::getUnits)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // average purchase of Nav.
        BigDecimal averagePurchaseNav = calculateAveragePurchaseNav(investedAmount, totalUnits);

        // current Net Asset Value
        BigDecimal currentNav = defaultZero(fund.getNav());

        // (currentWorth of Investment = Total Units × Fund's current NAV).
        BigDecimal currentWorth = totalUnits.multiply(currentNav);

        // Gain/Loss = difference of investedAmount and currentWorth of Investment.
        BigDecimal unrealizedGainLoss = currentWorth.subtract(investedAmount);

        // Return of Investment = (unrealizedGainLoss * 100 / investedAmount)%
        BigDecimal returnPercentage = calculatePercentage(unrealizedGainLoss, investedAmount);

        return FundHoldingResponse.builder()
                .fundId(fund.getId())
                .fundCode(fund.getFundCode())
                .fundName(fund.getFundName())
                .currency(fund.getCurrency())
                .investedAmount(scaleMoney(investedAmount))
                .totalUnits(
                        totalUnits.setScale(
                                UNIT_SCALE,
                                RoundingMode.HALF_UP
                        )
                )
                .averagePurchaseNav(
                        averagePurchaseNav.setScale(
                                UNIT_SCALE,
                                RoundingMode.HALF_UP
                        )
                )
                .currentNav(
                        currentNav.setScale(
                                UNIT_SCALE,
                                RoundingMode.HALF_UP
                        )
                )
                .currentValue(scaleMoney(currentWorth))
                .unrealizedGainLoss(scaleMoney(unrealizedGainLoss))
                .returnPercentage(returnPercentage)
                .build();
    }

    /**
     * Calculates the average purchase navigation for fund investments.
     *
     * @param investedAmount The total invested amount in the fund.
     * @param totalUnits     Total units of the fund.
     * @return The calculated average purchase navigation value.
     */
    private BigDecimal calculateAveragePurchaseNav(BigDecimal investedAmount, BigDecimal totalUnits) {
        if (totalUnits.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return investedAmount.divide(totalUnits, UNIT_SCALE, RoundingMode.HALF_UP);
    }


    /**
     * Calculates the percentage difference between two numbers.
     *
     * <p>This method takes two numbers as input, `gainLoss` and `baseAmount`, and returns the percentage difference between them.</p>
     *
     * @param gainLoss   The amount of gain or loss.
     * @param baseAmount The total amount from which to calculate the percentage change.
     * @return A BigDecimal representing the percentage difference. Returns 0 if the base amount is zero.
     */
    private BigDecimal calculatePercentage(BigDecimal gainLoss, BigDecimal baseAmount) {
        if (baseAmount.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO.setScale(PERCENTAGE_SCALE, RoundingMode.UNNECESSARY);
        }

        return gainLoss.multiply(BigDecimal.valueOf(100))
                .divide(baseAmount, PERCENTAGE_SCALE, RoundingMode.HALF_UP);
    }

    /**
     * Scale for monetary values
     */
    private BigDecimal defaultZero(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    /**
     * Scales a given {@code BigDecimal} value to the specified scale using half-up rounding_MODE.
     *
     * @param value The original {@code BigDecimal} value to be scaled
     *              The number of decimal places to round the value to (e.g., 2 for two decimal places)
     * @return A new {@code BigDecimal} with the value scaled to the specified scale using half-up rounding_MODE.
     */
    private BigDecimal scaleMoney(BigDecimal value) {
        return value.setScale(MONEY_SCALE, RoundingMode.HALF_UP);
    }


}
