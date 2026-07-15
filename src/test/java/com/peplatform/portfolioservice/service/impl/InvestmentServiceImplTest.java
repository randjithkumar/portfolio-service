package com.peplatform.portfolioservice.service.impl;

import com.peplatform.portfolioservice.common.enums.FundStatus;
import com.peplatform.portfolioservice.common.enums.InvestmentStatus;
import com.peplatform.portfolioservice.dto.request.InvestmentRequest;
import com.peplatform.portfolioservice.dto.request.InvestmentStatusUpdateRequest;
import com.peplatform.portfolioservice.dto.response.InvestmentResponse;
import com.peplatform.portfolioservice.entity.Fund;
import com.peplatform.portfolioservice.entity.Investment;
import com.peplatform.portfolioservice.entity.Investor;
import com.peplatform.portfolioservice.exception.ResourceNotFoundException;
import com.peplatform.portfolioservice.repository.FundRepository;
import com.peplatform.portfolioservice.repository.InvestmentRepository;
import com.peplatform.portfolioservice.repository.InvestorRepository;
import com.peplatform.portfolioservice.service.api.OutboxService;
import com.peplatform.portfolioservice.service.validator.InvestmentBusinessValidator;
import com.peplatform.portfolioservice.service.validator.InvestmentStatusTransitionValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvestmentServiceImplTest {

    private static final String IDEMPOTENCY_KEY = "investment-20260715-0001";

    @Mock
    private InvestmentRepository investmentRepository;
    @Mock
    private InvestorRepository investorRepository;
    @Mock
    private FundRepository fundRepository;
    @Mock
    private InvestmentBusinessValidator businessValidator;
    @Mock
    private InvestmentStatusTransitionValidator transitionValidator;
    @Mock
    private OutboxService outboxService;

    @InjectMocks
    private InvestmentServiceImpl service;

    private Investor investor;
    private Fund fund;
    private InvestmentRequest request;

    @BeforeEach
    void setUp() {
        investor = Investor.builder()
                .id(11L)
                .investorCode("INV-011")
                .fullName("Institutional Investor")
                .committedAmount(new BigDecimal("500000.00"))
                .build();
        fund = Fund.builder()
                .id(21L)
                .fundCode("PE-GROWTH")
                .fundName("Private Equity Growth Fund")
                .currency("USD")
                .nav(new BigDecimal("125.0000"))
                .inceptionDate(LocalDate.of(2020, 1, 1))
                .status(FundStatus.ACTIVE)
                .build();
        request = new InvestmentRequest();
        request.setInvestorId(investor.getId());
        request.setFundId(fund.getId());
        request.setAmount(new BigDecimal("250000.00"));
    }

    @Nested
    class CreateInvestment {
        @Test
        @DisplayName("Creates PENDING investment, calculates units, and writes outbox event")
        void createsInvestmentAndOutboxEvent() {
            List<Investment> existing = List.of();
            when(investorRepository.findById(11L)).thenReturn(Optional.of(investor));
            when(fundRepository.findById(21L)).thenReturn(Optional.of(fund));
            when(investmentRepository.findByInvestorId(11L)).thenReturn(existing);
            when(investmentRepository.save(any(Investment.class))).thenAnswer(invocation -> {
                Investment saved = invocation.getArgument(0);
                saved.setId(101L);
                saved.setInvestmentDate(LocalDateTime.of(2026, 7, 13, 10, 0));
                saved.setCreatedAt(LocalDateTime.of(2026, 7, 13, 10, 0));
                return saved;
            });

            InvestmentResponse response = service.createInvestment(request);

            ArgumentCaptor<Investment> captor = ArgumentCaptor.forClass(Investment.class);
            verify(investmentRepository).save(captor.capture());
            Investment persisted = captor.getValue();
            assertThat(persisted.getInvestor()).isSameAs(investor);
            assertThat(persisted.getFund()).isSameAs(fund);
            assertThat(persisted.getAmount()).isEqualByComparingTo("250000.00");
            assertThat(persisted.getNav()).isEqualByComparingTo("125.0000");
            assertThat(persisted.getUnits()).isEqualByComparingTo("2000.0000");
            assertThat(persisted.getStatus()).isEqualTo(InvestmentStatus.PENDING);

            verify(businessValidator).validateNewInvestment(
                    investor, fund, request.getAmount(), existing);
            verify(outboxService).saveEvent(
                    eq("INVESTMENT"), eq("101"), eq("portfolio.investment.created.v1"), eq("101"), any());
            assertThat(response.getId()).isEqualTo(101L);
            assertThat(response.getUnits()).isEqualByComparingTo("2000.0000");
            assertThat(response.getStatus()).isEqualTo(InvestmentStatus.PENDING);
        }

        @Test
        void roundsUnitsToFourDecimalPlaces() {
            request.setAmount(new BigDecimal("1000.00"));
            fund.setNav(new BigDecimal("3.0000"));
            when(investorRepository.findById(11L)).thenReturn(Optional.of(investor));
            when(fundRepository.findById(21L)).thenReturn(Optional.of(fund));
            when(investmentRepository.findByInvestorId(11L)).thenReturn(List.of());
            when(investmentRepository.save(any())).thenAnswer(invocation -> {
                Investment investment = invocation.getArgument(0);
                investment.setId(102L);
                return investment;
            });

            InvestmentResponse response = service.createInvestment(request);

            assertThat(response.getUnits()).isEqualByComparingTo("333.3333");
        }

        @Test
        void failsWhenInvestorDoesNotExist() {
            when(investorRepository.findById(11L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.createInvestment(request))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Investor not found with id: 11");
            verifyNoInteractions(fundRepository, businessValidator, outboxService);
            verify(investmentRepository, never()).save(any());
        }

        @Test
        void failsWhenFundDoesNotExist() {
            when(investorRepository.findById(11L)).thenReturn(Optional.of(investor));
            when(fundRepository.findById(21L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.createInvestment(request))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Fund not found with id: 21");
            verifyNoInteractions(businessValidator, outboxService);
            verify(investmentRepository, never()).save(any());
        }

        @Test
        void doesNotPersistWhenBusinessValidationFails() {
            when(investorRepository.findById(11L)).thenReturn(Optional.of(investor));
            when(fundRepository.findById(21L)).thenReturn(Optional.of(fund));
            when(investmentRepository.findByInvestorId(11L)).thenReturn(List.of());
            doThrow(new IllegalArgumentException("validation failed"))
                    .when(businessValidator)
                    .validateNewInvestment(any(), any(), any(), any());

            assertThatThrownBy(() -> service.createInvestment(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("validation failed");
            verify(investmentRepository, never()).save(any());
            verifyNoInteractions(outboxService);
        }
    }

    @Nested
    class QueryInvestment {
        @Test
        void getsInvestmentById() {
            Investment investment = completeInvestment(101L, InvestmentStatus.CONFIRMED);
            when(investmentRepository.findById(101L)).thenReturn(Optional.of(investment));

            InvestmentResponse response = service.getInvestmentById(101L);

            assertThat(response.getId()).isEqualTo(101L);
            assertThat(response.getInvestorCode()).isEqualTo("INV-011");
        }

        @Test
        void throwsWhenInvestmentIsMissing() {
            when(investmentRepository.findById(999L)).thenReturn(Optional.empty());
            assertThatThrownBy(() -> service.getInvestmentById(999L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Investment not found with id: 999");
        }

        @Test
        void mapsAllInvestments() {
            when(investmentRepository.findAll()).thenReturn(List.of(
                    completeInvestment(101L, InvestmentStatus.CONFIRMED),
                    completeInvestment(102L, InvestmentStatus.PENDING)));

            assertThat(service.getAllInvestments())
                    .extracting(InvestmentResponse::getId)
                    .containsExactly(101L, 102L);
        }

        @Test
        void getsInvestmentsByInvestorAndFund() {
            Investment investment = completeInvestment(101L, InvestmentStatus.CONFIRMED);
            when(investmentRepository.findByInvestorId(11L)).thenReturn(List.of(investment));
            when(investmentRepository.findByFundId(21L)).thenReturn(List.of(investment));

            assertThat(service.getAllInvestmentByInvestorId(11L)).hasSize(1);
            assertThat(service.getAllInvestmentByFundId(21L)).hasSize(1);
        }
    }

    @Nested
    class UpdateStatus {
        @Test
        void updatesStatusAndWritesOutboxEvent() {
            Investment investment = completeInvestment(101L, InvestmentStatus.PENDING);
            InvestmentStatusUpdateRequest update = new InvestmentStatusUpdateRequest();
            update.setStatus(InvestmentStatus.VALIDATED);
            when(investmentRepository.findById(101L)).thenReturn(Optional.of(investment));
            when(investmentRepository.save(investment)).thenReturn(investment);

            InvestmentResponse response = service.updateInvestmentStatus(101L, update);

            verify(transitionValidator).validate(InvestmentStatus.PENDING, InvestmentStatus.VALIDATED);
            verify(outboxService).saveEvent(
                    eq("INVESTMENT"), eq("101"), eq("portfolio.investment.status-changed.v1"), eq("101"), any());
            assertThat(response.getStatus()).isEqualTo(InvestmentStatus.VALIDATED);
        }

        @Test
        void doesNotSaveWhenTransitionValidationFails() {
            Investment investment = completeInvestment(101L, InvestmentStatus.CONFIRMED);
            InvestmentStatusUpdateRequest update = new InvestmentStatusUpdateRequest();
            update.setStatus(InvestmentStatus.CANCELLED);
            when(investmentRepository.findById(101L)).thenReturn(Optional.of(investment));
            doThrow(new IllegalStateException("invalid transition"))
                    .when(transitionValidator)
                    .validate(InvestmentStatus.CONFIRMED, InvestmentStatus.CANCELLED);

            assertThatThrownBy(() -> service.updateInvestmentStatus(101L, update))
                    .isInstanceOf(IllegalStateException.class);
            verify(investmentRepository, never()).save(any());
            verifyNoInteractions(outboxService);
        }
    }

    private Investment completeInvestment(Long id, InvestmentStatus status) {
        Investment investment = Investment.builder()
                .id(id)
                .investor(investor)
                .fund(fund)
                .amount(new BigDecimal("250000.00"))
                .nav(new BigDecimal("125.0000"))
                .units(new BigDecimal("2000.0000"))
                .status(status)
                .investmentDate(LocalDateTime.of(2026, 7, 13, 10, 0))
                .build();
        investment.setCreatedAt(LocalDateTime.of(2026, 7, 13, 10, 0));
        return investment;
    }
}
