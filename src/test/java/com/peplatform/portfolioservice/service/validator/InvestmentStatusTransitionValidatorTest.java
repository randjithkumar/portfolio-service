package com.peplatform.portfolioservice.service.validator;

import com.peplatform.portfolioservice.common.enums.InvestmentStatus;
import com.peplatform.portfolioservice.exception.BusinessRuleException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InvestmentStatusTransitionValidatorTest {

    private final InvestmentStatusTransitionValidator validator =
            new InvestmentStatusTransitionValidator();

    @ParameterizedTest(name = "{0} -> {1} is allowed")
    @MethodSource("allowedTransitions")
    void acceptsAllowedTransitions(InvestmentStatus current, InvestmentStatus requested) {
        assertThatCode(() -> validator.validate(current, requested))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Rejects an unchanged status")
    void rejectsSameStatus() {
        assertThatThrownBy(() -> validator.validate(
                InvestmentStatus.PENDING, InvestmentStatus.PENDING))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage("Investment is already in status: PENDING");
    }

    @ParameterizedTest(name = "{0} -> {1} is rejected")
    @MethodSource("invalidTransitions")
    void rejectsInvalidTransitions(InvestmentStatus current, InvestmentStatus requested) {
        assertThatThrownBy(() -> validator.validate(current, requested))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("Invalid investment status transition");
    }

    private static Stream<Arguments> allowedTransitions() {
        return Stream.of(
                Arguments.of(InvestmentStatus.PENDING, InvestmentStatus.VALIDATED),
                Arguments.of(InvestmentStatus.PENDING, InvestmentStatus.FAILED),
                Arguments.of(InvestmentStatus.PENDING, InvestmentStatus.CANCELLED),
                Arguments.of(InvestmentStatus.VALIDATED, InvestmentStatus.CONFIRMED),
                Arguments.of(InvestmentStatus.VALIDATED, InvestmentStatus.FAILED),
                Arguments.of(InvestmentStatus.VALIDATED, InvestmentStatus.CANCELLED),
                Arguments.of(InvestmentStatus.CONFIRMED, InvestmentStatus.REVERSED)
        );
    }

    private static Stream<Arguments> invalidTransitions() {
        return Stream.of(
                Arguments.of(InvestmentStatus.PENDING, InvestmentStatus.CONFIRMED),
                Arguments.of(InvestmentStatus.VALIDATED, InvestmentStatus.REVERSED),
                Arguments.of(InvestmentStatus.CONFIRMED, InvestmentStatus.CANCELLED),
                Arguments.of(InvestmentStatus.FAILED, InvestmentStatus.PENDING),
                Arguments.of(InvestmentStatus.CANCELLED, InvestmentStatus.VALIDATED),
                Arguments.of(InvestmentStatus.REVERSED, InvestmentStatus.CONFIRMED)
        );
    }
}
