package com.peplatform.portfolioservice.service.impl;

import com.peplatform.portfolioservice.common.util.CorrelationIdUtils;
import com.peplatform.portfolioservice.dto.request.FundNavUpdateRequest;
import com.peplatform.portfolioservice.dto.request.FundRequest;
import com.peplatform.portfolioservice.dto.request.FundStatusUpdateRequest;
import com.peplatform.portfolioservice.dto.response.FundResponse;
import com.peplatform.portfolioservice.entity.Fund;
import com.peplatform.portfolioservice.exception.BusinessRuleException;
import com.peplatform.portfolioservice.exception.DuplicateResourceException;
import com.peplatform.portfolioservice.exception.ResourceNotFoundException;
import com.peplatform.portfolioservice.kafka.config.KafkaTopics;
import com.peplatform.portfolioservice.kafka.event.FundNavUpdatedEvent;
import com.peplatform.portfolioservice.mapper.FundMapper;
import com.peplatform.portfolioservice.repository.FundRepository;
import com.peplatform.portfolioservice.repository.InvestmentRepository;
import com.peplatform.portfolioservice.security.util.SecurityUtils;
import com.peplatform.portfolioservice.service.api.FundService;
import com.peplatform.portfolioservice.service.api.OutboxService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Implementation of the {@link FundService} interface.
 * Handles the business logic for fund management.
 *
 * @author Randjith
 * @version 1.0.0
 * @since 9/7/2026 12:44 pm
 */
@Service
@RequiredArgsConstructor
@Transactional
public class FundServiceImpl implements FundService {

    /**
     * Repository for accessing fund data.
     */
    private final FundRepository fundRepository;

    /**
     * Repository for accessing investment data.
     */
    private final InvestmentRepository investmentRepository;


//    /**
//     * Domain event publisher responsible for emitting various portfolio events.
//     */
//    private final PortfolioDomainEventPublisher eventPublisher;


    /**
     * Service for handling outbox operations related to financial transactions and other events related to the application's functionality.
     */
    private final OutboxService outboxService;

    /**
     * Creates a new fund with the provided details.
     *
     * @param request The request containing all the information for the new fund.
     * @return A response indicating the result of the fund creation.
     */
    @Override
    @Transactional
    public FundResponse createFund(FundRequest request) {
        if (fundRepository.existsByFundCode(request.getFundCode())) {
            throw new DuplicateResourceException(
                    "Fund code already exists: "
                            + request.getFundCode()
            );
        }

        Fund savedFund =
                fundRepository.save(FundMapper.toEntity(request));

        return FundMapper.toResponse(savedFund);
    }

    /**
     * Updates the fund information such as its name, navigation details, and status.
     *
     * @param id      The ID of the fund to update.
     * @param request An update request containing the new details for the fund.
     * @return A `FundResponse` object representing the updated fund.
     */
    @Override
    @Transactional
    public FundResponse updateFund(Long id, FundRequest request) {

        Fund fund = findFund(id);

        if (fundRepository.existsByFundCodeAndIdNot(
                request.getFundCode(),
                id
        )) {
            throw new DuplicateResourceException(
                    "Fund code already exists: "
                            + request.getFundCode()
            );
        }

        FundMapper.updateEntity(fund, request);

        return FundMapper.toResponse(fundRepository.save(fund)
        );
    }

    /**
     * Retrieves a list of all funds.
     *
     * <p>
     * This method retrieves all funds from the database, maps each fund to a `FundResponse` object using the `FundMapper`, and returns a list of these `FundResponse` objects.
     * </p>
     *
     * @return A list of `FundResponse` objects representing all Funds in the system.
     */
    @Override
    public List<FundResponse> getAllFunds() {
        return fundRepository.findAll()
                .stream()
                .map(FundMapper::toResponse)
                .toList();
    }

    /**
     * Retrieves a fund by its ID.
     * <p>
     * Finds the fund by the specified {@code id} and returns it in a {@link FundResponse}.
     *
     * @param id The ID of the fund to retrieve.
     * @return A {@link FundResponse} object containing the details of the fund, or `null` if no fund is found.
     */
    @Override
    public FundResponse getFundById(Long id) {
        return FundMapper.toResponse(findFund(id));
    }

    /**
     * Updates the navigation information of a fund.
     *
     * @param id      The ID of the fund to update.
     * @param request The update request containing the new navigation details for the fund.
     * @return The updated fund response.
     */
    @Override
    @Transactional
    public FundResponse updateFundNav(Long id, FundNavUpdateRequest request) {
        Fund fund = findFund(id);

        BigDecimal previousNav = fund.getNav();

        fund.setNav(request.getNav());

        Fund updatedFund = fundRepository.save(fund);

        FundNavUpdatedEvent event = FundNavUpdatedEvent.create(
                CorrelationIdUtils.getOrCreate(),
                updatedFund.getId(),
                updatedFund.getFundCode(),
                previousNav,
                updatedFund.getNav(),
                updatedFund.getCurrency(),
                SecurityUtils.currentUsername()
        );
//        eventPublisher.publish(event);
        outboxService.saveEvent(
                "FUND",
                updatedFund.getId().toString(),
                KafkaTopics.FUND_NAV_UPDATED,
                updatedFund.getId().toString(),
                event
        );

        return FundMapper.toResponse(updatedFund);
    }

    /**
     * Updates the status of a fund.
     *
     * @param id      The ID of the fund to update.
     * @param request The fund status update request containing the new status information for the fund.
     * @return The updated fund response.
     */
    @Override
    @Transactional
    public FundResponse updateFundStatus(Long id, FundStatusUpdateRequest request) {
        Fund fund = findFund(id);
        fund.setStatus(request.getStatus());

        return FundMapper.toResponse(
                fundRepository.save(fund)
        );
    }


    /**
     * Deletes a fund based on the provided ID.
     *
     * <p>
     * This method finds the fund by the specified ID and deletes it if no investment records exist for the fund. It throws a {@link BusinessRuleException} if there is an existing investment record for the fund.
     * </p>
     *
     * @param id The ID of the fund to delete.
     */
    @Override
    public void deleteFund(Long id) {
        Fund fund = findFund(id);

        if (investmentRepository.existsByFundId(id)) {
            throw new BusinessRuleException(
                    "Fund cannot be deleted because "
                            + "investment records exist"
            );
        }
        fundRepository.delete(fund);
    }

    /**
     * Finds a fund by its ID.
     *
     * @param id The ID of the fund to find.
     * @return A `FundResponse` object containing the details of the fund, or `null` if no fund is found.
     */
    private Fund findFund(Long id) {
        return fundRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Fund not found with id: " + id
                        )
                );
    }
}