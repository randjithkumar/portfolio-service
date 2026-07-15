package com.peplatform.portfolioservice.integration.marketdata.service;

import com.peplatform.portfolioservice.integration.marketdata.dto.MarketPriceResponse;

public interface MarketDataService {
    MarketPriceResponse getLatestPrice(String symbol);
}
