package com.peplatform.portfolioservice.service.impl;

import com.peplatform.portfolioservice.common.enums.InvestmentStatus;
import com.peplatform.portfolioservice.common.util.CorrelationIdUtils;
import com.peplatform.portfolioservice.dto.request.InvestmentRequest;
import com.peplatform.portfolioservice.dto.request.InvestmentStatusUpdateRequest;
import com.peplatform.portfolioservice.dto.response.InvestmentResponse;
import com.peplatform.portfolioservice.entity.Fund;
import com.peplatform.portfolioservice.entity.Investment;
import com.peplatform.portfolioservice.entity.Investor;
import com.peplatform.portfolioservice.exception.ResourceNotFoundException;
import com.peplatform.portfolioservice.kafka.config.KafkaTopics;
import com.peplatform.portfolioservice.kafka.event.InvestmentCreatedEvent;
import com.peplatform.portfolioservice.kafka.event.InvestmentStatusChangedEvent;
import com.peplatform.portfolioservice.mapper.InvestmentMapper;
import com.peplatform.portfolioservice.repository.FundRepository;
import com.peplatform.portfolioservice.repository.InvestmentRepository;
import com.peplatform.portfolioservice.repository.InvestorRepository;
import com.peplatform.portfolioservice.security.util.SecurityUtils;
import com.peplatform.portfolioservice.service.api.InvestmentService;
import com.peplatform.portfolioservice.service.api.OutboxService;
import com.peplatform.portfolioservice.service.validator.InvestmentBusinessValidator;
import com.peplatform.portfolioservice.service.validator.InvestmentStatusTransitionValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


/**
 * Service implementation for managing investments within the system.
 * <p>
 * This service provides methods to create, query, update, and delete investments by handling business rules such as ensuring investment amounts are valid and that all referenced entities exist in the database.
 * </p>
 *
 * @author Randjith
 * @version 1.0.0
 * @email randjithkumar@no-reply.github.com
 * @date 2026.07.11
 * @since 0.0.1-SNAPSHOT
 */
@Service
@RequiredArgsConstructor
@Transactional
public class InvestmentServiceImpl implements InvestmentService {

    /**
     * This is a field representing the number of units in scale
     */
    private static final int UNITS_SCALE = 4;

    /**
     * Repository for accessing investment data.
     */
    private final InvestmentRepository investmentRepository;

    /**
     * Repository for accessing investor data.
     */
    private final InvestorRepository investorRepository;

    /**
     * Repository for accessing fund data.
     */
    private final FundRepository fundRepository;

    /**
     * Business validator for investment operations.
     */
    private final InvestmentBusinessValidator businessValidator;

    /**
     * @NotNull The validator for validation transitions between investment statuses.
     */
    private final InvestmentStatusTransitionValidator transitionValidator;


//    /**
//     * Publisher responsible for distributing domain events.
//     */
//    private final PortfolioDomainEventPublisher eventPublisher;

    /**
     * Publisher responsible for distributing domain events.
     */
    private final OutboxService outboxService;

    /**
     * @param request The investment request containing details of the investment.
     * @return InvestmentResponse`
     */
    @Override
    public InvestmentResponse createInvestment(InvestmentRequest request) {
        return createInvestment(UUID.randomUUID().toString(), request);
    }

    /**
     * Creates a new investment for the given investor and fund, validating the investment request.
     * <p>
     * This method processes the creation of a new investment based on the provided request. It first checks if the investor or fund exists, then calculates the appropriate units for the investment, and finally saves and creates an event to notify other systems about this transaction.
     *
     * @param idempotencyKey A unique identifier for this request to prevent duplicate transactions.
     * @param request        The investment request containing detailed instructions for creating the investment.
     * @return An {@link InvestmentResponse} that contains details about the newly created investment.
     */
    @Override
    public InvestmentResponse createInvestment(String idempotencyKey, InvestmentRequest request) {
        String normalizedKey = normalizeIdempotencyKey(idempotencyKey);

        return investmentRepository.findByIdempotencyKey(normalizedKey)
                .map(InvestmentMapper::toResponse)
                .orElseGet(() -> createNewInvestment(normalizedKey, request));
        /*
        Investor investor = investorRepository.findById(request.getInvestorId())
                .orElseThrow(() -> new ResourceNotFoundException("Investor not found with id: " + request.getInvestorId()));

        Fund fund = fundRepository.findById(request.getFundId())
                .orElseThrow(() -> new ResourceNotFoundException("Fund not found with id: " + request.getFundId()));

        List<Investment> existingInvestments =
                investmentRepository.findByInvestorId(
                        investor.getId()
                );

        businessValidator.validateNewInvestment(
                investor,
                fund,
                request.getAmount(),
                existingInvestments
        );

        BigDecimal units = calculateUnits(
                request.getAmount(),
                fund.getNav()
        );

        Investment investment = Investment.builder()
                .idempotencyKey(idempotencyKey)
                .investor(investor)
                .fund(fund)
                .amount(request.getAmount())
                .nav(fund.getNav())
                .units(units)
                .status(InvestmentStatus.PENDING)
                .build();

        Investment savedInvestment =
                investmentRepository.save(investment);
        InvestmentCreatedEvent event = InvestmentCreatedEvent.create(
                CorrelationIdUtils.getOrCreate(),
                savedInvestment.getId(),
                investor.getId(),
                investor.getInvestorCode(),
                fund.getId(),
                fund.getFundCode(),
                savedInvestment.getAmount(),
                savedInvestment.getNav(),
                savedInvestment.getUnits(),
                savedInvestment.getStatus()
        );
//        eventPublisher.publish(event);
        outboxService.saveEvent(
                "INVESTMENT", savedInvestment.getId().toString(),
                KafkaTopics.INVESTMENT_CREATED, savedInvestment.getId().toString(), event);

        return InvestmentMapper.toResponse(savedInvestment); */
        /*var units = request.getAmount().divide(fund.getNav(), 4, RoundingMode.HALF_UP);

        Investment investment = Investment.builder()
                .idempotencyKey(idempotencyKey)
                .investor(investor)
                .fund(fund)
                .amount(request.getAmount())
                .nav(fund.getNav())
                .units(units)
                .status(InvestmentStatus.CONFIRMED)
                .build();

        Investment savedInvestment = investmentRepository.save(investment);

        return InvestmentMapper.toResponse(savedInvestment);*/
    }

    private InvestmentResponse createNewInvestment(String idempotencyKey, InvestmentRequest request) {
        Investor investor = investorRepository.findById(request.getInvestorId())
                .orElseThrow(() -> new ResourceNotFoundException("Investor not found with id: " + request.getInvestorId()));

        Fund fund = fundRepository.findById(request.getFundId())
                .orElseThrow(() -> new ResourceNotFoundException("Fund not found with id: " + request.getFundId()));

        List<Investment> existingInvestments =
                investmentRepository.findByInvestorId(
                        investor.getId()
                );

        businessValidator.validateNewInvestment(
                investor,
                fund,
                request.getAmount(),
                existingInvestments
        );

        BigDecimal units = calculateUnits(
                request.getAmount(),
                fund.getNav()
        );

        Investment investment = Investment.builder()
                .idempotencyKey(idempotencyKey)
                .investor(investor)
                .fund(fund)
                .amount(request.getAmount())
                .nav(fund.getNav())
                .units(units)
                .status(InvestmentStatus.PENDING)
                .investmentDate(LocalDateTime.now())
                .build();

        Investment savedInvestment =
                investmentRepository.save(investment);
        InvestmentCreatedEvent event = InvestmentCreatedEvent.create(
                CorrelationIdUtils.getOrCreate(),
                savedInvestment.getId(),
                investor.getId(),
                investor.getInvestorCode(),
                fund.getId(),
                fund.getFundCode(),
                savedInvestment.getAmount(),
                savedInvestment.getNav(),
                savedInvestment.getUnits(),
                savedInvestment.getStatus()
        );
//        eventPublisher.publish(event);
        outboxService.saveEvent(
                "INVESTMENT", savedInvestment.getId().toString(),
                KafkaTopics.INVESTMENT_CREATED, savedInvestment.getId().toString(), event);

        return InvestmentMapper.toResponse(savedInvestment);
    }

    /**
     * @param id The ID of the investment to retrieve.
     * @return InvestmentResponse
     */
    @Override
    @Transactional(readOnly = true)
    public InvestmentResponse getInvestmentById(Long id) {
        Investment investment = findInvestment(id);

        return InvestmentMapper.toResponse(investment);
    }

    /**
     * @return list of all InvestmentResponse.
     */
    @Override
    @Transactional(readOnly = true)
    public List<InvestmentResponse> getAllInvestments() {
        return investmentRepository.findAll()
                .stream()
                .map(InvestmentMapper::toResponse)
                .toList();
    }

    /**
     * @param investorId The ID of the investor whose investments are to be retrieved.
     * @return list of all InvestmentResponse.
     */
    @Override
    @Transactional(readOnly = true)
    public List<InvestmentResponse> getAllInvestmentByInvestorId(Long investorId) {
        return investmentRepository.findByInvestorId(investorId)
                .stream()
                .map(InvestmentMapper::toResponse)
                .toList();
    }

    /**
     * @param fundId The ID of the fund whose investments are to be retrieved.
     * @return list of all InvestmentResponse.
     */
    @Override
    @Transactional(readOnly = true)
    public List<InvestmentResponse> getAllInvestmentByFundId(Long fundId) {
        return investmentRepository.findByFundId(fundId)
                .stream()
                .map(InvestmentMapper::toResponse)
                .toList();
    }

    /**
     * Updates the status of an investment.
     * <p>
     * This method takes the ID of the investment to be updated and the new status details as input. It updates the status of the specified investment and returns a response object containing the updated investment details or an error message if the update fails.
     *
     * @param id      The ID of the investment to update
     * @param request The request object containing the status details of the investment
     * @return A response object containing the updated investment details or detailed error information in case of failure
     */
    @Override
    public InvestmentResponse updateInvestmentStatus(Long id, InvestmentStatusUpdateRequest request) {
        Investment investment = findInvestment(id);

        InvestmentStatus previousStatus = investment.getStatus();

        transitionValidator.validate(
                previousStatus,
                request.getStatus()
        );

        investment.setStatus(request.getStatus());

        Investment updatedInvestment =
                investmentRepository.save(investment);
        InvestmentStatusChangedEvent event = InvestmentStatusChangedEvent.create(
                CorrelationIdUtils.getOrCreate(),
                updatedInvestment.getId(),
                updatedInvestment.getInvestor().getId(),
                updatedInvestment.getFund().getId(),
                previousStatus,
                updatedInvestment.getStatus(),
                SecurityUtils.currentUsername()
        );
//        eventPublisher.publish(event);

        outboxService.saveEvent(
                "INVESTMENT", updatedInvestment.getId().toString(),
                KafkaTopics.INVESTMENT_STATUS_CHANGED, updatedInvestment.getId().toString(), event
        );

        return InvestmentMapper.toResponse(updatedInvestment);
    }

    /**
     * Generates a normalized, trimmed idempotency key from the provided string.
     *
     * @param idempotencyKey the input string potentially containing special characters or leading/trailing spaces
     * @return a normalized version of the idempotency key that meets the specified constraints on length and non-blank characters
     */
    private String normalizeIdempotencyKey(String idempotencyKey) {
        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            throw new IllegalArgumentException("Idempotency-Key header must not be blank");
        }
        String normalized = idempotencyKey.trim();
        if (normalized.length() > 100) {
            throw new IllegalArgumentException("Idempotency-Key must not exceed 100 characters");
        }
        return normalized;
    }

    /**
     * Finds an existing investment by its ID.
     *
     * @param id The ID of the investment to retrieve.
     * @return An {@code InvestmentResponse} object representing the found investment.
     */
    private Investment findInvestment(Long id) {
        return investmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Investment not found with id: " + id
                        )
                );
    }

    /**
     * Ensures that the specified investment exists.
     * <p>This method verifies whether an investment with the given ID currently exists in the system.</p>
     *
     * @param investorId The ID of the investor to check
     */
    private void ensureInvestorExists(Long investorId) {
        if (!investorRepository.existsById(investorId)) {
            throw new ResourceNotFoundException(
                    "Investor not found with id: " + investorId
            );
        }
    }

    /**
     * Ensures that a fund exists with the given ID.
     *
     * @param fundId The ID of the fund to verify
     */
    private void ensureFundExists(Long fundId) {
        if (!fundRepository.existsById(fundId)) {
            throw new ResourceNotFoundException(
                    "Fund not found with id: " + fundId
            );
        }
    }

    private BigDecimal calculateUnits(
            BigDecimal amount,
            BigDecimal nav
    ) {
        return amount.divide(
                nav,
                UNITS_SCALE,
                RoundingMode.HALF_UP
        );
    }
}
