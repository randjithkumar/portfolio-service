package com.peplatform.portfolioservice.service.impl;

import com.peplatform.portfolioservice.dto.request.InvestorRequest;
import com.peplatform.portfolioservice.dto.response.InvestorResponse;
import com.peplatform.portfolioservice.entity.Investor;
import com.peplatform.portfolioservice.exception.BusinessRuleException;
import com.peplatform.portfolioservice.exception.DuplicateResourceException;
import com.peplatform.portfolioservice.exception.ResourceNotFoundException;
import com.peplatform.portfolioservice.mapper.InvestorMapper;
import com.peplatform.portfolioservice.repository.InvestmentRepository;
import com.peplatform.portfolioservice.repository.InvestorRepository;
import com.peplatform.portfolioservice.service.api.InvestorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of the {@link InvestorService} interface.
 * Handles the business logic for investor management.
 *
 * @author Randjith
 * @version 1.0.0
 * @since 9/7/2026 3:44 pm
 */
@Service
@RequiredArgsConstructor
@Transactional
public class InvestorServiceImpl implements InvestorService {

    /**
     * Repository for accessing investor data.
     */
    private final InvestorRepository investorRepository;

    /**
     * Repository for accessing investor data.
     */
    private final InvestmentRepository investmentRepository;

    /**
     * {@inheritDoc}
     *
     * @throws DuplicateResourceException if an investor with the same code already exists
     */
    @Override
    public InvestorResponse createInvestor(InvestorRequest investorRequest) {
        if (investorRepository.existsByInvestorCode(investorRequest.getInvestorCode())) {
            throw new DuplicateResourceException("Investor code already exists: " + investorRequest.getInvestorCode());
        }

        Investor investor = InvestorMapper.toEntity(investorRequest);
        Investor savedInvestor = investorRepository.save(investor);

        return InvestorMapper.toResponse(savedInvestor);
    }

    /**
     * {@inheritDoc}
     *
     * @throws ResourceNotFoundException if no investor is found with the given ID
     */
    @Override
    @Transactional(readOnly = true)
    public InvestorResponse getInvestorById(Long id) {
        Investor investor = findInvestor(id);

        return InvestorMapper.toResponse(investor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<InvestorResponse> getAllInvestors() {
        return investorRepository.findAll().stream().map(InvestorMapper::toResponse).toList();
    }

    /**
     * Updates an investor's information.
     *
     * <p> Retrieves the investor by the provided ID, updates the investor details based on the given {@link InvestorRequest},
     * and returns the updated investor.</p>
     *
     * @param id              The unique identifier of the investor to update.
     * @param investorRequest The request containing the new investor information.
     * @return The updated investor response.
     */
    @Override
    public InvestorResponse updateInvestor(Long id, InvestorRequest investorRequest) {
        Investor investor = findInvestor(id);

        if (investorRepository.existsByInvestorCodeAndIdNot(investorRequest.getInvestorCode(), id)) {
            throw new DuplicateResourceException("Investor code already exists: " + investorRequest.getInvestorCode());
        }

        InvestorMapper.updateEntity(investor, investorRequest);

        Investor updatedInvestor = investorRepository.save(investor);

        return InvestorMapper.toResponse(updatedInvestor);
    }

    /**
     * Deletes an investor from the system by their ID.
     *
     * <p>
     * Performs a business validation to ensure that no investment records exist for the given investor before deleting. If any such records do exist, throws a {@link BusinessRuleException}.
     * </p>
     *
     * @param id The unique identifier of the investor to delete.
     */
    @Override
    public void deleteInvestor(Long id) {
        Investor investor = findInvestor(id);

        if (investmentRepository.existsByInvestorId(id)) {
            throw new BusinessRuleException("Investor cannot be deleted because " + "investment records exist");
        }

        investorRepository.delete(investor);
    }

    /**
     * Retrieves the investor by the specified ID.
     * <p>
     * Finds the investor by the provided user ID and returns the investor's name.
     *
     * @param investorId The Investor ID
     * @return The Investor object.
     */
    private Investor findInvestor(Long investorId) {
        return investorRepository.findById(investorId).orElseThrow(() -> new ResourceNotFoundException("Investor not found with id: " + investorId));
    }
}