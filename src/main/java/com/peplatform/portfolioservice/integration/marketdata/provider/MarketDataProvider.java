package com.peplatform.portfolioservice.integration.marketdata.provider;

import com.peplatform.portfolioservice.integration.marketdata.dto.MarketPriceResponse;

public interface MarketDataProvider {

    MarketPriceResponse getLatestPrice(String symbol);

    String getProviderName();
}