package com.peplatform.portfolioservice.mapper;

import com.peplatform.portfolioservice.dto.response.InvestmentResponse;
import com.peplatform.portfolioservice.entity.Investment;

public class InvestmentMapper {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private InvestmentMapper() {
    }

    /**
     * Converts an {@link Investment} entity to an {@link InvestmentResponse} DTO.
     *
     * @param investment the investment entity to be converted
     * @return a new {@link InvestmentResponse} object populated with the data from the investment entity
     */
    public static InvestmentResponse toResponse(Investment investment) {
        return InvestmentResponse.builder()
                .id(investment.getId())
                .idempotencyKey(investment.getIdempotencyKey())
                .investorId(investment.getInvestor().getId())
                .investorCode(investment.getInvestor().getInvestorCode())
                .investorName(investment.getInvestor().getFullName())
                .fundId(investment.getFund().getId())
                .fundCode(investment.getFund().getFundCode())
                .fundName(investment.getFund().getFundName())
                .amount(investment.getAmount())
                .nav(investment.getNav())
                .units(investment.getUnits())
                .status(investment.getStatus())
                .investmentDate(investment.getInvestmentDate())
                .createdAt(investment.getCreatedAt())
                .build();
    }
}