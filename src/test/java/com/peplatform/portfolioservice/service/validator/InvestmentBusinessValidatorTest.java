package com.peplatform.portfolioservice.service.validator;

import com.peplatform.portfolioservice.common.enums.FundStatus;
import com.peplatform.portfolioservice.common.enums.InvestmentStatus;
import com.peplatform.portfolioservice.entity.Fund;
import com.peplatform.portfolioservice.entity.Investment;
import com.peplatform.portfolioservice.entity.Investor;
import com.peplatform.portfolioservice.exception.BusinessRuleException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InvestmentBusinessValidatorTest {

    private InvestmentBusinessValidator validator;
    private Investor investor;
    private Fund fund;

    @BeforeEach
    void setUp() {
        validator = new InvestmentBusinessValidator();
        investor = Investor.builder()
                .id(10L)
                .investorCode("INV-001")
                .committedAmount(new BigDecimal("100000.00"))
                .build();
        fund = Fund.builder()
                .id(20L)
                .fundCode("FUND-001")
                .nav(new BigDecimal("125.0000"))
                .status(FundStatus.ACTIVE)
                .build();
    }

    @Test
    @DisplayName("Accepts an investment within the remaining commitment")
    void acceptsValidInvestment() {
        Investment confirmed = investment("30000.00", InvestmentStatus.CONFIRMED);
        Investment reversed = investment("5000.00", InvestmentStatus.REVERSED);

        assertThatCode(() -> validator.validateNewInvestment(
                investor, fund, new BigDecimal("70000.00"), List.of(confirmed, reversed)))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Rejects null, zero, and negative amounts")
    void rejectsInvalidAmounts() {
        assertThatThrownBy(() -> validator.validateNewInvestment(investor, fund, null, List.of()))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage("Investment amount must be greater than zero");
        assertThatThrownBy(() -> validator.validateNewInvestment(investor, fund, BigDecimal.ZERO, List.of()))
                .isInstanceOf(BusinessRuleException.class);
        assertThatThrownBy(() -> validator.validateNewInvestment(investor, fund, new BigDecimal("-1.00"), List.of()))
                .isInstanceOf(BusinessRuleException.class);
    }

    @Test
    @DisplayName("Rejects investment into a non-active fund")
    void rejectsInactiveFund() {
        fund.setStatus(FundStatus.CLOSED);

        assertThatThrownBy(() -> validator.validateNewInvestment(
                investor, fund, new BigDecimal("1000.00"), List.of()))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("is not ACTIVE");
    }

    @Test
    @DisplayName("Rejects null or non-positive NAV")
    void rejectsInvalidNav() {
        fund.setNav(null);
        assertThatThrownBy(() -> validator.validateNewInvestment(
                investor, fund, new BigDecimal("1000.00"), List.of()))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("NAV");

        fund.setNav(BigDecimal.ZERO);
        assertThatThrownBy(() -> validator.validateNewInvestment(
                investor, fund, new BigDecimal("1000.00"), List.of()))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("NAV");
    }

    @Test
    @DisplayName("Counts only commitment-consuming statuses")
    void countsOnlyCommitmentConsumingStatuses() {
        List<Investment> investments = List.of(
                investment("20000.00", InvestmentStatus.PENDING),
                investment("15000.00", InvestmentStatus.VALIDATED),
                investment("25000.00", InvestmentStatus.CONFIRMED),
                investment("90000.00", InvestmentStatus.FAILED),
                investment("90000.00", InvestmentStatus.CANCELLED),
                investment("90000.00", InvestmentStatus.REVERSED)
        );

        assertThatCode(() -> validator.validateNewInvestment(
                investor, fund, new BigDecimal("40000.00"), investments))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Rejects an amount above remaining commitment")
    void rejectsAmountAboveRemainingCommitment() {
        List<Investment> existing = List.of(
                investment("60000.00", InvestmentStatus.CONFIRMED),
                investment("10000.00", InvestmentStatus.PENDING)
        );

        assertThatThrownBy(() -> validator.validateNewInvestment(
                investor, fund, new BigDecimal("30000.01"), existing))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("exceeds remaining commitment 30000.00");
    }

    @Test
    @DisplayName("Treats a null committed amount as zero")
    void treatsNullCommitmentAsZero() {
        investor.setCommittedAmount(null);

        assertThatThrownBy(() -> validator.validateNewInvestment(
                investor, fund, new BigDecimal("1.00"), List.of()))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("remaining commitment 0");
    }

    private Investment investment(String amount, InvestmentStatus status) {
        return Investment.builder()
                .amount(new BigDecimal(amount))
                .status(status)
                .build();
    }
}
