package com.peplatform.portfolioservice.mapper;

import com.peplatform.portfolioservice.dto.request.FundRequest;
import com.peplatform.portfolioservice.dto.response.FundResponse;
import com.peplatform.portfolioservice.entity.Fund;

/**
 * Utility class for mapping between {@link Fund} entity and its corresponding DTOs.
 *
 * @author Randjith
 * @version 1.0.0
 */
public class FundMapper {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private FundMapper() {
    }

    /**
     * Maps a {@link FundRequest} DTO to a {@link Fund} entity.
     *
     * @param request the fund creation request
     * @return the mapped fund entity
     */
    public static Fund toEntity(FundRequest request) {
        return Fund.builder()
                .fundCode(request.getFundCode())
                .fundName(request.getFundName())
                .assetClass(request.getAssetClass())
                .currency(request.getCurrency().toUpperCase())
                .nav(request.getNav())
                .inceptionDate(request.getInceptionDate())
                .status(request.getStatus())
                .build();
    }

    /**
     * Updates a given {@link Fund} entity based on the provided {@link FundRequest}.
     *
     * <p>This method performs several operations:
     * - Sets the fund code, name, asset class, currency, NAV, inception date, and status of the given {@link Fund} entity.
     * - Converts the currency string to uppercase.
     * - Handles any potential exceptions that may occur during the update process.
     *
     * @param fund    The {@link Fund} entity to be updated.
     * @param request The {@link FundRequest} containing the new values for the {@link Fund} entity.
     */
    public static void updateEntity(
            Fund fund,
            FundRequest request
    ) {
        fund.setFundCode(request.getFundCode());
        fund.setFundName(request.getFundName());
        fund.setAssetClass(request.getAssetClass());
        fund.setCurrency(request.getCurrency().toUpperCase());
        fund.setNav(request.getNav());
        fund.setInceptionDate(request.getInceptionDate());
        fund.setStatus(request.getStatus());
    }

    /**
     * Maps a {@link Fund} entity to a {@link FundResponse} DTO.
     *
     * @param fund the fund entity
     * @return the mapped fund response DTO
     */
    public static FundResponse toResponse(Fund fund) {
        return FundResponse.builder()
                .id(fund.getId())
                .fundCode(fund.getFundCode())
                .fundName(fund.getFundName())
                .assetClass(fund.getAssetClass())
                .currency(fund.getCurrency())
                .nav(fund.getNav())
                .inceptionDate(fund.getInceptionDate())
                .status(fund.getStatus())
                .createdAt(fund.getCreatedAt())
                .build();
    }
}