package com.peplatform.portfolioservice.mapper;

import com.peplatform.portfolioservice.dto.request.InvestorRequest;
import com.peplatform.portfolioservice.dto.response.InvestorResponse;
import com.peplatform.portfolioservice.entity.Investor;


/**
 * Investor Mapper class
 * <p>Provides operations related to investor data, including queries, inserts, updates, and deletes</p>
 *
 * @author Randjith
 * @version 1.0.0
 * @email "mailto:randjithkumar@no-reply@github.com"
 * @date 2026.07.11
 * @since 1.0.0
 **/
public class InvestorMapper {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private InvestorMapper() {
    }

    /**
     * Maps an {@link InvestorRequest} DTO to an {@link Investor} entity.
     *
     * @param request the investor creation request
     * @return the mapped investor entity
     */
    public static Investor toEntity(InvestorRequest request) {
        return Investor.builder()
                .investorCode(request.getInvestorCode())
                .fullName(request.getName())
                .email(request.getEmail())
                .country(request.getCountry())
                .riskProfile(request.getRiskProfile())
                .committedAmount(request.getCommittedAmount())
                .build();
    }

    /**
     * Updates the properties of an Investor entity based on a received InvestorRequest.
     *
     * @param investor The Investor object to be updated.
     * @param request  The InvestorRequest object containing the new values for the investor.
     */
    public static void updateEntity(
            Investor investor,
            InvestorRequest request
    ) {
        investor.setInvestorCode(request.getInvestorCode());
        investor.setFullName(request.getName());
        investor.setEmail(request.getEmail());
        investor.setCountry(request.getCountry());
        investor.setRiskProfile(request.getRiskProfile());
        investor.setCommittedAmount(request.getCommittedAmount());
    }

    /**
     * Maps an {@link Investor} entity to an {@link InvestorResponse} DTO.
     *
     * @param investor the investor entity
     * @return the mapped investor response DTO
     */
    public static InvestorResponse toResponse(Investor investor) {
        return InvestorResponse.builder()
                .id(investor.getId())
                .investorCode(investor.getInvestorCode())
                .name(investor.getFullName())
                .email(investor.getEmail())
                .country(investor.getCountry())
                .riskProfile(investor.getRiskProfile())
                .committedAmount(investor.getCommittedAmount())
                .createdAt(investor.getCreatedAt())
                .build();
    }
}