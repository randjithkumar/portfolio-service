package com.peplatform.portfolioservice.integration.marketdata.service;

import com.peplatform.portfolioservice.integration.marketdata.dto.MarketPriceResponse;
import com.peplatform.portfolioservice.integration.marketdata.exception.MarketDataProviderException;
import com.peplatform.portfolioservice.integration.marketdata.provider.FmpMarketDataProvider;
import com.peplatform.portfolioservice.integration.marketdata.provider.YahooMarketDataProvider;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Slf4j
@Service
public class MarketDataServiceImpl implements MarketDataService {

    private final FmpMarketDataProvider fmpProvider;
    private final YahooMarketDataProvider yahooProvider;

    public MarketDataServiceImpl(
            FmpMarketDataProvider fmpProvider,
            YahooMarketDataProvider yahooProvider
    ) {
        this.fmpProvider = fmpProvider;
        this.yahooProvider = yahooProvider;
    }

    @Override
    public MarketPriceResponse getLatestPrice(String symbol) {
        String normalizedSymbol = normalize(symbol);

        try {
            log.info(
                    "Requesting market price from primary provider: provider={}, symbol={}",
                    fmpProvider.getProviderName(),
                    normalizedSymbol
            );

            return fmpProvider.getLatestPrice(normalizedSymbol);

        } catch (CallNotPermittedException exception) {
            log.warn(
                    "FMP circuit breaker is open. Using Yahoo: symbol={}",
                    normalizedSymbol
            );

            return retrieveFromYahoo(normalizedSymbol, exception);

        } catch (MarketDataProviderException exception) {
            /*
             * Use Yahoo for FMP technical and configuration failures,
             * including a missing API key.
             *
             * Input validation happens before the provider call, so an
             * invalid symbol still fails immediately.
             */
            log.warn(
                    "FMP failed. Using Yahoo: symbol={}, retryable={}, reason={}",
                    normalizedSymbol,
                    exception.isRetryable(),
                    exception.getMessage()
            );

            return retrieveFromYahoo(normalizedSymbol, exception);

        } catch (RuntimeException exception) {
            /*
             * Protect against unexpected provider-client exceptions that
             * were not wrapped by FmpMarketDataProvider.
             */
            log.warn(
                    "Unexpected FMP failure. Using Yahoo: symbol={}, reason={}",
                    normalizedSymbol,
                    exception.getMessage()
            );

            return retrieveFromYahoo(normalizedSymbol, exception);
        }
    }

    private MarketPriceResponse retrieveFromYahoo(
            String symbol,
            Throwable primaryFailure
    ) {
        try {
            log.info(
                    "Requesting market price from secondary provider: provider={}, symbol={}",
                    yahooProvider.getProviderName(),
                    symbol
            );

            MarketPriceResponse response =
                    yahooProvider.getLatestPrice(symbol);

            log.info(
                    "Market price retrieved from secondary provider: "
                            + "provider={}, symbol={}, price={}",
                    yahooProvider.getProviderName(),
                    response.symbol(),
                    response.price()
            );

            return response;

        } catch (RuntimeException secondaryFailure) {
            secondaryFailure.addSuppressed(primaryFailure);

            log.error(
                    "Both market-data providers failed: "
                            + "primary={}, secondary={}, symbol={}, yahooReason={}",
                    fmpProvider.getProviderName(),
                    yahooProvider.getProviderName(),
                    symbol,
                    secondaryFailure.getMessage(),
                    secondaryFailure
            );

            throw secondaryFailure;
        }
    }

    private String normalize(String symbol) {
        if (symbol == null || symbol.isBlank()) {
            throw new IllegalArgumentException(
                    "Symbol must not be blank"
            );
        }

        String normalizedSymbol =
                symbol.trim().toUpperCase(Locale.ROOT);

        if (!normalizedSymbol.matches("[A-Z0-9.\\-^]{1,20}")) {
            throw new IllegalArgumentException(
                    "Invalid market symbol: " + symbol
            );
        }

        return normalizedSymbol;
    }
}